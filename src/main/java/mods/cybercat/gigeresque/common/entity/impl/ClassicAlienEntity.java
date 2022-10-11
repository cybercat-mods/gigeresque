package mods.cybercat.gigeresque.common.entity.impl;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.config.ConfigAccessor;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.enums.AlienAttackType;
import mods.cybercat.gigeresque.common.entity.ai.goal.classic.BuildNestGoal;
import mods.cybercat.gigeresque.common.entity.ai.goal.classic.ClassicAlienMeleeAttackGoal;
import mods.cybercat.gigeresque.common.entity.ai.goal.classic.FindNestGoal;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class ClassicAlienEntity extends AdultAlienEntity {

	private AnimationFactory animationFactory = new AnimationFactory(this);
	private int holdingCounter = 0;

	public ClassicAlienEntity(@NotNull EntityType<? extends AlienEntity> type, @NotNull World world) {
		super(type, world);
	}

	@Override
	public void travel(Vec3d movementInput) {
		this.navigation = (this.isSubmergedInWater() || this.isTouchingWater()) ? swimNavigation : landNavigation;
		this.moveControl = (this.submergedInWater || this.isTouchingWater()) ? swimMoveControl : landMoveControl;
		this.lookControl = (this.submergedInWater || this.isTouchingWater()) ? swimLookControl : landLookControl;

		if (canMoveVoluntarily() && this.isTouchingWater()) {
			updateVelocity(getMovementSpeed(), movementInput);
			move(MovementType.SELF, getVelocity());
			setVelocity(getVelocity().multiply(0.5));
			if (getTarget() == null) {
				setVelocity(getVelocity().add(0.0, -0.005, 0.0));
			}
		} else {
			super.travel(movementInput);
		}
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0)
				.add(EntityAttributes.GENERIC_ARMOR, 6.0).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.13000000417232513)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0 * Constants.getIsolationModeDamageBase())
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0)
				.add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE, 1.0);
	}

	@Override
	public void tick() {
		super.tick();

		// Attack logic

		if (attackProgress > 0) {
			attackProgress--;

			if (!world.isClient && attackProgress <= 0) {
				setCurrentAttackType(AlienAttackType.NONE);
			}
		}

		if (attackProgress == 0 && handSwinging) {
			attackProgress = 10;
		}

		if (!world.isClient && getCurrentAttackType() == AlienAttackType.NONE) {
			if (this.getVelocity().horizontalLength() == 0) {
				setCurrentAttackType(switch (random.nextInt(5)) {
				case 0 -> AlienAttackType.CLAW_LEFT;
				case 1 -> AlienAttackType.CLAW_RIGHT;
				case 2 -> AlienAttackType.TAIL_LEFT;
				case 3 -> AlienAttackType.TAIL_RIGHT;
				default -> AlienAttackType.CLAW_LEFT;
				});
			} else {
				setCurrentAttackType(switch (random.nextInt(5)) {
				case 0 -> AlienAttackType.CLAW_LEFT_MOVING;
				case 1 -> AlienAttackType.CLAW_RIGHT_MOVING;
				case 2 -> AlienAttackType.TAIL_LEFT_MOVING;
				case 3 -> AlienAttackType.TAIL_RIGHT_MOVING;
				default -> AlienAttackType.CLAW_LEFT_MOVING;
				});
			}
		}
		if (this.isAttacking()) {
			this.setPose(EntityPose.CROUCHING);
		} else {
			this.setPose(EntityPose.STANDING);
		}

		if (this.getTarget() != null) {
			Stream<BlockState> list = this.world.getStatesInBoxIfLoaded(this.getBoundingBox().expand(18.0, 18.0, 18.0));
			if (this.hasPassengers() && !list.anyMatch(NEST) && ConfigAccessor.isTargetAlienHost(this.getTarget())) {
				double yOffset = this.getEyeY()
						- ((this.getFirstPassenger().getEyeY() - this.getFirstPassenger().getBlockPos().getY()) / 2.0);
				double e = this.getFirstPassenger().getX()
						+ ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1);
				double f = this.getFirstPassenger().getZ()
						+ ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1);
				holdingCounter++;
				if (holdingCounter == 760) {
					this.getNavigation().stop();
					this.setIsExecuting(true);
					GeckoLib.LOGGER.debug(holdingCounter);
					this.setAttacking(false);
				}
				if (holdingCounter == 850) {
					this.getFirstPassenger().kill();
					this.getFirstPassenger().world.addImportantParticle(Particles.BLOOD, e, yOffset, f, 0.0, -0.15,
							0.0);
					this.getFirstPassenger().setInvisible(false);
				}
				if (holdingCounter >= 880) {
					this.setIsExecuting(false);
					holdingCounter = 0;
				}
			}
		}
	}

	@Override
	public float getGrowthMultiplier() {
		return GigeresqueConfig.alienGrowthMultiplier;
	}

	@Override
	public boolean tryAttack(Entity target) {
		float additionalDamage = switch (getCurrentAttackType().genericAttackType) {
		case TAIL -> 3.0f;
		default -> 0.0f;
		};

		if (target instanceof LivingEntity && !world.isClient) {
			switch (getCurrentAttackType().genericAttackType) {
			case NONE -> {
			}
			case CLAW -> {
				if (target instanceof PlayerEntity playerEntity && this.random.nextInt(7) == 0) {
					playerEntity.dropItem(playerEntity.getInventory().getMainHandStack(), true, false);
					playerEntity.getInventory().removeOne(playerEntity.getInventory().getMainHandStack());
				}
			}
			case TAIL -> {
				var armorItems = StreamSupport.stream(target.getArmorItems().spliterator(), false)
						.collect(Collectors.toList());
				if (!armorItems.isEmpty()) {
					armorItems.get(new Random().nextInt(armorItems.size())).damage(10, this, it -> {
					});
				}
			}
			}
		}

		target.damage(DamageSource.mob(this), additionalDamage);

		return super.tryAttack(target);
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(2, new ClassicAlienMeleeAttackGoal(this, 3.0, false));
		this.goalSelector.add(2, new FindNestGoal(this));
		this.goalSelector.add(2, new BuildNestGoal(this));
	}

	public void grabTarget(Entity entity) {
		if (entity == this.getTarget() && !entity.hasPassenger(this)
				&& !(entity.getBlockStateAtPos().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS)) {
			entity.startRiding(this, true);
			if (entity instanceof ServerPlayerEntity) {
				((ServerPlayerEntity) entity).networkHandler.sendPacket(new EntityPassengersSetS2CPacket(entity));
			}
		}
	}

	@Override
	public void updatePassengerPosition(Entity passenger) {
		super.updatePassengerPosition(passenger);
		if (passenger instanceof MobEntity) {
			MobEntity mobEntity = (MobEntity) passenger;
			this.bodyYaw = mobEntity.bodyYaw;
		}
		passenger.setPosition(this.getX(), this.getY() + 0.6, this.getZ());
		passenger.speed = 0;
		if (passenger instanceof LivingEntity) {
			((LivingEntity) passenger).bodyYaw = this.bodyYaw;
		}
	}

	/*
	 * ANIMATIONS
	 */

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		var velocityLength = this.getVelocity().horizontalLength();
		var isDead = this.dead || this.getHealth() < 0.01 || this.isDead();
		if (velocityLength >= 0.000000001 && !this.isCrawling() && this.isExecuting() == false && !isDead
				&& this.isStatis() == false) {
			if (!this.submergedInWater && this.isExecuting() == false) {
				if (lastLimbDistance > 0.35F && this.getFirstPassenger() == null) {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("run", true)
							.addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()), false));
					return PlayState.CONTINUE;
				} else if (this.isExecuting() == false && !this.isCrawling() && this.onGround) {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true)
							.addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()), false));
					return PlayState.CONTINUE;
				}
			} else {
				if (this.submergedInWater && this.isExecuting() == false) {
					if (this.isAttacking() && !this.hasPassengers()) {
						event.getController().setAnimation(new AnimationBuilder().addAnimation("rush_swim", true));
						return PlayState.CONTINUE;
					} else {
						event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_water", true));
						return PlayState.CONTINUE;
					}
				}
			}
		} else if (getCurrentAttackType() != AlienAttackType.NONE && attackProgress > 0 && !this.hasPassengers()
				&& this.isExecuting() == false) {
			event.getController().setAnimation(new AnimationBuilder()
					.addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()), true));
			return PlayState.CONTINUE;
		} else if (this.isCrawling() && this.isExecuting() == false && this.isStatis() == false) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("crawl", true));
			return PlayState.CONTINUE;
		} else if (isDead) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("death", false));
			return PlayState.CONTINUE;
		} else if (this.isExecuting() == true && this.hasPassengers() && this.isStatis() == false) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("execution", false));
			return PlayState.CONTINUE;
		} else {
			if (this.submergedInWater && !isSearching && !this.isAttacking() && !this.hasPassengers()
					&& this.isExecuting() == false && this.isStatis() == false) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_water", true));
				return PlayState.CONTINUE;
			} else if (!this.submergedInWater && isSearching && !this.isAttacking() && !this.hasPassengers()
					&& this.isExecuting() == false && this.isStatis() == false && !isDead) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("ambient", false));
				return PlayState.CONTINUE;
			} else if (this.isStatis() == true || this.isAiDisabled() && !isDead) {
				event.getController().setAnimation(
						new AnimationBuilder().addAnimation("stasis_enter", false).addAnimation("stasis_loop", true));
				return PlayState.CONTINUE;
			} else if (!this.submergedInWater && this.isExecuting() == false && !this.hasPassengers()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_land", true));
				return PlayState.CONTINUE;
			}
		}
		event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_land", true));
		return PlayState.CONTINUE;
	}

	private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
		if (this.dataTracker.get(IS_BREAKING) == true) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("left_claw", true));
			return PlayState.CONTINUE;
		}
		if (getCurrentAttackType() != AlienAttackType.NONE && attackProgress > 0 && !this.hasPassengers()
				&& this.isExecuting() == false) {
			event.getController().setAnimation(new AnimationBuilder()
					.addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()), true));
			return PlayState.CONTINUE;
		}
		if (this.hasPassengers() && this.isExecuting() == false) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("kidnap", true));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}

	private <E extends IAnimatable> PlayState hissPredicate(AnimationEvent<E> event) {
		var isDead = this.dead || this.getHealth() < 0.01 || this.isDead();
		if (this.dataTracker.get(IS_HISSING) == true && !this.hasPassengers() && this.isExecuting() == false
				&& !isDead) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("hiss", true));
			return PlayState.CONTINUE;
		}

		return PlayState.STOP;
	}

	private <ENTITY extends IAnimatable> void soundStepListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("footstepSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_FOOTSTEP,
						SoundCategory.HOSTILE, 0.5F, 1.0F, true);
			}
		}
		if (event.sound.matches("handstepSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HANDSTEP,
						SoundCategory.HOSTILE, 0.5F, 1.0F, true);
			}
		}
		if (event.sound.matches("ambientSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_AMBIENT,
						SoundCategory.HOSTILE, 1.0F, 1.0F, true);
			}
		}
		if (event.sound.matches("thudSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_DEATH_THUD,
						SoundCategory.HOSTILE, 1.0F, 1.0F, true);
			}
		}
		if (event.sound.matches("biteSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HEADBITE,
						SoundCategory.HOSTILE, 1.0F, 1.0F, true);
			}
		}
		if (event.sound.matches("crunchSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_CRUNCH,
						SoundCategory.HOSTILE, 1.0F, 1.0F, true);
			}
		}
	}

	private <ENTITY extends IAnimatable> void soundAttackListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("clawSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_CLAW,
						SoundCategory.HOSTILE, 0.25F, 1.0F, true);
			}
		}
		if (event.sound.matches("tailSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_TAIL,
						SoundCategory.HOSTILE, 0.25F, 1.0F, true);
			}
		}
	}

	private <ENTITY extends IAnimatable> void soundHissListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("hissSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HISS,
						SoundCategory.HOSTILE, 1.0F, 1.0F, true);
			}
		}
	}

	@Override
	public void registerControllers(AnimationData data) {
		AnimationController<ClassicAlienEntity> main = new AnimationController<ClassicAlienEntity>(this, "controller",
				10f, this::predicate);
		main.registerSoundListener(this::soundStepListener);
		data.addAnimationController(main);
		AnimationController<ClassicAlienEntity> attacking = new AnimationController<ClassicAlienEntity>(this,
				"attackController", 5f, this::attackPredicate);
		attacking.registerSoundListener(this::soundAttackListener);
		data.addAnimationController(attacking);
		AnimationController<ClassicAlienEntity> hissing = new AnimationController<ClassicAlienEntity>(this,
				"hissController", 10f, this::hissPredicate);
		hissing.registerSoundListener(this::soundHissListener);
		data.addAnimationController(hissing);
	}

	@Override
	public AnimationFactory getFactory() {
		return animationFactory;
	}

}
