package mods.cybercat.gigeresque.common.entity.impl;

import java.util.Random;
import java.util.SplittableRandom;
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
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class ClassicAlienEntity extends AdultAlienEntity {

	private AnimationFactory animationFactory = GeckoLibUtil.createFactory(this);

	public ClassicAlienEntity(@NotNull EntityType<? extends AlienEntity> type, @NotNull Level world) {
		super(type, world);
	}

	@Override
	public void travel(Vec3 movementInput) {
		this.navigation = (this.isUnderWater() || this.isInWater()) ? swimNavigation : landNavigation;
		this.moveControl = (this.wasEyeInWater || this.isInWater()) ? swimMoveControl : landMoveControl;
		this.lookControl = (this.wasEyeInWater || this.isInWater()) ? swimLookControl : landLookControl;

		if (isEffectiveAi() && this.isInWater()) {
			moveRelative(getSpeed(), movementInput);
			move(MoverType.SELF, getDeltaMovement());
			setDeltaMovement(getDeltaMovement().scale(0.5));
			if (getTarget() == null) {
				setDeltaMovement(getDeltaMovement().add(0.0, -0.005, 0.0));
			}
		} else {
			super.travel(movementInput);
		}
	}

	public static AttributeSupplier.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, 100.0).add(Attributes.ARMOR, 6.0)
				.add(Attributes.ARMOR_TOUGHNESS, 0.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0)
				.add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.13000000417232513)
				.add(Attributes.ATTACK_DAMAGE, 7.0 * Constants.getIsolationModeDamageBase())
				.add(Attributes.ATTACK_KNOCKBACK, 1.0).add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE, 1.0);
	}

	@Override
	public void tick() {
		super.tick();

		// Attack logic

		if (attackProgress > 0) {
			attackProgress--;

			if (!level.isClientSide && attackProgress <= 0) {
				setCurrentAttackType(AlienAttackType.NONE);
			}
		}

		if (attackProgress == 0 && swinging) {
			attackProgress = 10;
		}

		if (!level.isClientSide && getCurrentAttackType() == AlienAttackType.NONE) {
			if (this.isCrawling() || this.isInWater() && this.getDeltaMovement().horizontalDistance() < 0.000001) {
				setCurrentAttackType(switch (this.getAttckingState()) {
				case 0 -> AlienAttackType.CLAW_LEFT;
				case 1 -> AlienAttackType.CLAW_RIGHT;
				case 2 -> AlienAttackType.TAIL_LEFT;
				case 3 -> AlienAttackType.TAIL_RIGHT;
				default -> AlienAttackType.CLAW_LEFT;
				});
			} else {
				setCurrentAttackType(switch (this.getAttckingState()) {
				case 0 -> AlienAttackType.CLAW_LEFT_MOVING;
				case 1 -> AlienAttackType.CLAW_RIGHT_MOVING;
				case 2 -> AlienAttackType.TAIL_LEFT_MOVING;
				case 3 -> AlienAttackType.TAIL_RIGHT_MOVING;
				default -> AlienAttackType.CLAW_LEFT_MOVING;
				});
			}
		}
		if (this.isAggressive()) {
			this.setPose(Pose.CROUCHING);
		} else {
			this.setPose(Pose.STANDING);
		}

		if (this.getFirstPassenger() != null) {
			if (this.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) {
				this.getFirstPassenger().setPos(this.getX(), this.getY()+0.2, this.getZ());
				this.getFirstPassenger().removeVehicle();
			}
		}

		if (this.getTarget() != null) {
			Stream<BlockState> list = this.level
					.getBlockStatesIfLoaded(this.getBoundingBox().inflate(18.0, 18.0, 18.0));
			if (this.isVehicle() && !list.anyMatch(NEST) && ConfigAccessor.isTargetAlienHost(this.getTarget())) {
				double yOffset = this.getEyeY()
						- ((this.getFirstPassenger().getEyeY() - this.getFirstPassenger().blockPosition().getY())
								/ 2.0);
				double e = this.getFirstPassenger().getX()
						+ ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1);
				double f = this.getFirstPassenger().getZ()
						+ ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1);
				holdingCounter++;
				if (holdingCounter == 760) {
					this.getNavigation().stop();
					this.setIsExecuting(true);
					GeckoLib.LOGGER.debug(holdingCounter);
					this.setAggressive(false);
				}
				if (holdingCounter >= 850) {
					this.getFirstPassenger().hurt(GigDamageSources.EXECUTION, Float.MAX_VALUE);
					this.getFirstPassenger().level.addAlwaysVisibleParticle(Particles.BLOOD, e, yOffset, f, 0.0, -0.15,
							0.0);
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
	public boolean doHurtTarget(Entity target) {
		float additionalDamage = switch (getCurrentAttackType().genericAttackType) {
		case TAIL -> 3.0f;
		case EXECUTION -> Float.MAX_VALUE;
		default -> 0.0f;
		};

		if (target instanceof LivingEntity && !level.isClientSide) {
			switch (getAttckingState()) {
			case 1 -> {
				if (target instanceof Player playerEntity && this.random.nextInt(7) == 0) {
					playerEntity.drop(playerEntity.getInventory().getSelected(), true, false);
					playerEntity.getInventory().removeItem(playerEntity.getInventory().getSelected());
				}
				target.hurt(DamageSource.mobAttack(this), additionalDamage);
				return super.doHurtTarget(target);
			}
			case 2 -> {
				if (target instanceof Player playerEntity && this.random.nextInt(7) == 0) {
					playerEntity.drop(playerEntity.getInventory().getSelected(), true, false);
					playerEntity.getInventory().removeItem(playerEntity.getInventory().getSelected());
				}
				target.hurt(DamageSource.mobAttack(this), additionalDamage);
				return super.doHurtTarget(target);
			}
			case 3 -> {
				var armorItems = StreamSupport.stream(target.getArmorSlots().spliterator(), false)
						.collect(Collectors.toList());
				if (!armorItems.isEmpty()) {
					armorItems.get(new Random().nextInt(armorItems.size())).hurtAndBreak(10, this, it -> {
					});
				}
				target.hurt(DamageSource.mobAttack(this), additionalDamage);
				return super.doHurtTarget(target);
			}
			case 4 -> {
				var armorItems = StreamSupport.stream(target.getArmorSlots().spliterator(), false)
						.collect(Collectors.toList());
				if (!armorItems.isEmpty()) {
					armorItems.get(new Random().nextInt(armorItems.size())).hurtAndBreak(10, this, it -> {
					});
				}
				target.hurt(DamageSource.mobAttack(this), additionalDamage);
				return super.doHurtTarget(target);
			}
			}
		}
		return super.doHurtTarget(target);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(2, new ClassicAlienMeleeAttackGoal(this, 3.0, false));
		this.goalSelector.addGoal(2, new FindNestGoal(this));
		this.goalSelector.addGoal(2, new BuildNestGoal(this));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, LivingEntity.class, 2.0F));
	}

	@Override
	public void positionRider(Entity passenger) {
		super.positionRider(passenger);
		if (passenger instanceof LivingEntity) {
			LivingEntity mob = (LivingEntity) passenger;
			SplittableRandom random = new SplittableRandom();
			mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 10, true, true));
			float f = Mth.sin(this.yBodyRot * ((float) Math.PI / 180));
			float g = Mth.cos(this.yBodyRot * ((float) Math.PI / 180));
			passenger.setPos(this.getX() + (double) ((this.isExecuting() == true ? -2.4f : -1.85f) * f),
					this.getY() + (double) (this.isExecuting() == true ? random.nextFloat(0.74F, 0.75f)
							: random.nextFloat(0.14F, 0.15F)),
					this.getZ() - (double) ((this.isExecuting() == true ? -2.4f : -1.85f) * g));
			mob.yBodyRot = this.yBodyRot;
		}
	}

	/*
	 * ANIMATIONS
	 */
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
		if (event.isMoving() && !this.isCrawling() && this.isExecuting() == false && !isDead && this.isStatis() == false
				&& !this.swinging) {
			if (!this.isInWater() && this.isExecuting() == false) {
				if (animationSpeedOld > 0.35F && this.getFirstPassenger() == null) {
					event.getController()
							.setAnimation(new AnimationBuilder().addAnimation("run", EDefaultLoopTypes.LOOP)
									.addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()),
											EDefaultLoopTypes.LOOP));
					return PlayState.CONTINUE;
				} else if (!this.isCrawling()) {
					if (this.isVehicle()) {
						event.getController()
								.setAnimation(new AnimationBuilder()
										.addAnimation("walk_carrying", EDefaultLoopTypes.LOOP)
										.addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()),
												EDefaultLoopTypes.LOOP));
						event.getController().setAnimationSpeed(animationSpeedOld * 4);
						return PlayState.CONTINUE;
					} else {
						event.getController()
								.setAnimation(new AnimationBuilder().addAnimation("walk", EDefaultLoopTypes.LOOP)
										.addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()),
												EDefaultLoopTypes.LOOP));
						event.getController().setAnimationSpeed(animationSpeedOld * 4);
						return PlayState.CONTINUE;
					}
				}
			} else {
				if (this.wasEyeInWater && this.isExecuting() == false && !this.isVehicle()) {
					if (this.isAggressive() && !this.isVehicle()) {
						event.getController()
								.setAnimation(new AnimationBuilder().addAnimation("rush_swim", EDefaultLoopTypes.LOOP));
						return PlayState.CONTINUE;
					} else {
						event.getController().setAnimation(
								new AnimationBuilder().addAnimation("idle_water", EDefaultLoopTypes.LOOP));
						return PlayState.CONTINUE;
					}
				}
			}
		} else if (this.isCrawling() && this.isExecuting() == false && this.isStatis() == false && !this.isVehicle()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("crawl", EDefaultLoopTypes.LOOP));
			return PlayState.CONTINUE;
		} else if (isDead && !this.isVehicle()) {
			event.getController()
					.setAnimation(new AnimationBuilder().addAnimation("death", EDefaultLoopTypes.PLAY_ONCE));
			return PlayState.CONTINUE;
		} else if (this.isExecuting() == true && this.isVehicle() && this.isStatis() == false) {
			if (this.isVehicle()) {
				event.getController().setAnimation(
						new AnimationBuilder().addAnimation("execution_carry", EDefaultLoopTypes.PLAY_ONCE));
			} else {
				event.getController().setAnimation(
						new AnimationBuilder().addAnimation("execution_grab", EDefaultLoopTypes.PLAY_ONCE));
			}
			return PlayState.CONTINUE;
		} else {
			if (this.wasEyeInWater && !isSearching && !this.isAggressive() && !this.isVehicle()
					&& this.isExecuting() == false && this.isStatis() == false) {
				event.getController()
						.setAnimation(new AnimationBuilder().addAnimation("idle_water", EDefaultLoopTypes.LOOP));
				return PlayState.CONTINUE;
			} else if (!this.wasEyeInWater && isSearching && !this.isAggressive() && !this.isVehicle()
					&& this.isExecuting() == false && this.isStatis() == false && !isDead) {
				event.getController()
						.setAnimation(new AnimationBuilder().addAnimation("ambient", EDefaultLoopTypes.PLAY_ONCE));
				return PlayState.CONTINUE;
			} else if (this.isStatis() == true || this.isNoAi() && !isDead && !this.isVehicle()) {
				event.getController()
						.setAnimation(new AnimationBuilder().addAnimation("stasis_enter", EDefaultLoopTypes.PLAY_ONCE)
								.addAnimation("stasis_loop", EDefaultLoopTypes.LOOP));
				return PlayState.CONTINUE;
			} else if (!this.wasEyeInWater && this.isExecuting() == false && !this.isVehicle()) {
				event.getController()
						.setAnimation(new AnimationBuilder().addAnimation("idle_land", EDefaultLoopTypes.LOOP));
				return PlayState.CONTINUE;
			}
		}
		return PlayState.CONTINUE;
	}

	private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
		if (this.entityData.get(IS_BREAKING) == true && !this.isVehicle()) {
			event.getController()
					.setAnimation(new AnimationBuilder().addAnimation("left_claw", EDefaultLoopTypes.LOOP));
			return PlayState.CONTINUE;
		}
		if (getCurrentAttackType() != AlienAttackType.NONE && attackProgress > 0 && !this.isVehicle()
				&& this.isExecuting() == false) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(
					AlienAttackType.animationMappings.get(getCurrentAttackType()), EDefaultLoopTypes.LOOP));
			return PlayState.CONTINUE;
		}
		if (this.isVehicle() && this.isExecuting() == false) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("kidnap", EDefaultLoopTypes.LOOP));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}

	private <E extends IAnimatable> PlayState hissPredicate(AnimationEvent<E> event) {
		var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
		if (this.entityData.get(IS_HISSING) == true && !this.isVehicle() && this.isExecuting() == false && !isDead) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("hiss", EDefaultLoopTypes.LOOP));
			return PlayState.CONTINUE;
		}

		return PlayState.STOP;
	}

	private <ENTITY extends IAnimatable> void soundStepListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("footstepSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
						GigSounds.ALIEN_FOOTSTEP, SoundSource.HOSTILE, 0.5F, 1.0F, true);
			}
		}
		if (event.sound.matches("handstepSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
						GigSounds.ALIEN_HANDSTEP, SoundSource.HOSTILE, 0.5F, 1.0F, true);
			}
		}
		if (event.sound.matches("ambientSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
						GigSounds.ALIEN_AMBIENT, SoundSource.HOSTILE, 1.0F, 1.0F, true);
			}
		}
		if (event.sound.matches("thudSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
						GigSounds.ALIEN_DEATH_THUD, SoundSource.HOSTILE, 1.0F, 1.0F, true);
			}
		}
		if (event.sound.matches("biteSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
						GigSounds.ALIEN_HEADBITE, SoundSource.HOSTILE, 1.0F, 1.0F, true);
			}
		}
		if (event.sound.matches("crunchSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
						GigSounds.ALIEN_CRUNCH, SoundSource.HOSTILE, 1.0F, 1.0F, true);
			}
		}
	}

	private <ENTITY extends IAnimatable> void soundAttackListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("clawSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_CLAW,
						SoundSource.HOSTILE, 0.25F, 1.0F, true);
			}
		}
		if (event.sound.matches("tailSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_TAIL,
						SoundSource.HOSTILE, 0.25F, 1.0F, true);
			}
		}
	}

	private <ENTITY extends IAnimatable> void soundHissListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("hissSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HISS,
						SoundSource.HOSTILE, 1.0F, 1.0F, true);
			}
		}
	}

	@Override
	public void registerControllers(AnimationData data) {
		AnimationController<ClassicAlienEntity> main = new AnimationController<ClassicAlienEntity>(this, "controller",
				5f, this::predicate);
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
