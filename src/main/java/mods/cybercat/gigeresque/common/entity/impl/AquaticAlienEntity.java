package mods.cybercat.gigeresque.common.entity.impl;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.enums.AlienAttackType;
import mods.cybercat.gigeresque.common.entity.ai.goal.AlienMeleeAttackGoal;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation.LoopType;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class AquaticAlienEntity extends AdultAlienEntity {

	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private final GroundPathNavigation landNavigation = new GroundPathNavigation(this, level);
	private final AmphibiousNavigation swimNavigation = new AmphibiousNavigation(this, level);
	private final MoveControl landMoveControl = new MoveControl(this);
	private final LookControl landLookControl = new LookControl(this);
	private final SmoothSwimmingMoveControl swimMoveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.7f, 1.0f,
			false);
	private final SmoothSwimmingLookControl swimLookControl = new SmoothSwimmingLookControl(this, 10);

	public AquaticAlienEntity(EntityType<? extends AlienEntity> type, Level world) {
		super(type, world);
		maxUpStep = 1.0f;

		navigation = swimNavigation;
		moveControl = swimMoveControl;
		lookControl = swimLookControl;
		setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, 90.0).add(Attributes.ARMOR, 4.0)
				.add(Attributes.ARMOR_TOUGHNESS, 0.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0)
				.add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.2500000417232513)
				.add(Attributes.ATTACK_DAMAGE, 7.0).add(Attributes.ATTACK_KNOCKBACK, 1.0)
				.add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE, 0.85);
	}

	@Override
	public float getGrowthMultiplier() {
		return GigeresqueConfig.aquaticAlienGrowthMultiplier;
	}

	@Override
	public void travel(Vec3 movementInput) {
		this.navigation = (this.isUnderWater() || this.isInWater()) ? swimNavigation : landNavigation;
		this.moveControl = (this.wasEyeInWater || this.isInWater()) ? swimMoveControl : landMoveControl;
		this.lookControl = (this.wasEyeInWater || this.isInWater()) ? swimLookControl : landLookControl;

		if (this.tickCount % 10 == 0) {
			this.refreshDimensions();
		}

		if (isEffectiveAi() && this.isInWater()) {
			moveRelative(getSpeed(), movementInput);
			move(MoverType.SELF, getDeltaMovement());
			setDeltaMovement(getDeltaMovement().scale(0.75));
			if (getTarget() == null) {
				setDeltaMovement(getDeltaMovement().add(0.0, -0.005, 0.0));
			}
		} else {
			super.travel(movementInput);
		}
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public PathNavigation createNavigation(Level world) {
		return swimNavigation;
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	protected void jumpInLiquid(TagKey<Fluid> fluid) {
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return this.wasEyeInWater ? super.getDimensions(pose).scale(1.0f, 0.5f) : super.getDimensions(pose);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(2, new AlienMeleeAttackGoal(this, 1.5, false));
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
			setCurrentAttackType(switch (random.nextInt(5)) {
			case 0 -> AlienAttackType.CLAW_LEFT_MOVING;
			case 1 -> AlienAttackType.CLAW_RIGHT_MOVING;
			case 2 -> AlienAttackType.TAIL_LEFT_MOVING;
			case 3 -> AlienAttackType.TAIL_RIGHT_MOVING;
			default -> AlienAttackType.CLAW_LEFT_MOVING;
			});
		}
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		float additionalDamage = switch (getCurrentAttackType().genericAttackType) {
		case TAIL -> 3.0f;
		case EXECUTION -> Float.MAX_VALUE;
		default -> 0.0f;
		};

		if (target instanceof LivingEntity && !level.isClientSide) {
			switch (getCurrentAttackType().genericAttackType) {
			case NONE -> {
			}
			case CLAW -> {
				if (target instanceof Player playerEntity && this.random.nextInt(7) == 0) {
					playerEntity.drop(playerEntity.getInventory().getSelected(), true, false);
					playerEntity.getInventory().removeItem(playerEntity.getInventory().getSelected());
				}
			}
			case TAIL -> {
				var armorItems = StreamSupport.stream(target.getArmorSlots().spliterator(), false)
						.collect(Collectors.toList());
				if (!armorItems.isEmpty()) {
					armorItems.get(new Random().nextInt(armorItems.size())).hurtAndBreak(10, this, it -> {
					});
				}
			}
			case EXECUTION -> {
				double yOffset = this.getEyeY() - ((target.getEyeY() - target.blockPosition().getY()) / 2.0);
				double e = target.getX()
						+ ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1);
				double f = target.getZ()
						+ ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1);
				holdingCounter++;
				if (holdingCounter == 760) {
					this.getNavigation().stop();
					this.setIsExecuting(true);
					GeckoLib.LOGGER.debug(holdingCounter);
					this.setAggressive(false);
				}
				if (holdingCounter == 850) {
					target.hurt(GigDamageSources.EXECUTION, Float.MAX_VALUE);
					target.level.addAlwaysVisibleParticle(Particles.BLOOD, e, yOffset, f, 0.0, -0.15, 0.0);
					this.setIsExecuting(false);
					holdingCounter = 0;
				}
				return super.doHurtTarget(target);
			}
			}
		}

		target.hurt(DamageSource.mobAttack(this), additionalDamage);

		return super.doHurtTarget(target);
	}

	/*
	 * ANIMATIONS
	 */
	@Override
	public void registerControllers(AnimatableManager<?> manager) {
		manager.addController(new AnimationController<>(this, "livingController", 5, event -> {

			var velocityLength = this.getDeltaMovement().horizontalDistance();
			var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
			if (this.isUnderWater() && this.wasTouchingWater) {
				if (this.isAggressive() && velocityLength > 0.0 && !isDead && this.isStatis() == false) {
					event.getController().setAnimation(RawAnimation.begin().thenLoop("rush_swim"));
					return PlayState.CONTINUE;
				} else if (!this.isAggressive() && velocityLength > 0.0 && !isDead && this.isStatis() == false) {
					event.getController().setAnimation(RawAnimation.begin().thenLoop("swim"));
					return PlayState.CONTINUE;
				} else if (isDead) {
					event.getController().setAnimation(RawAnimation.begin().thenLoop("death"));
					return PlayState.CONTINUE;
				} else {
					event.getController().setAnimation(RawAnimation.begin().thenLoop("idle_water"));
					return PlayState.CONTINUE;
				}
			} else {
				if (this.isAggressive() && velocityLength > 0.0 && !isDead && this.isStatis() == false) {
					event.getController().setAnimation(RawAnimation.begin().thenLoop("rush_crawl"));
					return PlayState.CONTINUE;
				} else if (!this.isAggressive() && velocityLength > 0.0 && !isDead && this.isStatis() == false) {
					event.getController().setAnimation(RawAnimation.begin().thenLoop("crawl"));
					return PlayState.CONTINUE;
				} else if (isSearching && !this.isAggressive() && !isDead && this.isStatis() == false) {
					event.getController().setAnimation(RawAnimation.begin().then("ambient", LoopType.PLAY_ONCE));
					return PlayState.CONTINUE;
				} else if (isDead) {
					event.getController().setAnimation(RawAnimation.begin().thenLoop("death"));
					return PlayState.CONTINUE;
				} else if (this.isStatis() == true || this.isNoAi()) {
					event.getController().setAnimation(RawAnimation.begin().thenLoop("stasis"));
					return PlayState.CONTINUE;
				} else if (this.isStatis() == false) {
					event.getController().setAnimation(RawAnimation.begin().thenLoop("idle_land2"));
					return PlayState.CONTINUE;
				}
				return PlayState.CONTINUE;
			}
		}).setSoundKeyframeHandler(event -> {
			if (event.getKeyframeData().getSound().matches("stepSoundkey")) {
				if (this.level.isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
							GigSounds.AQUA_LANDMOVE, SoundSource.HOSTILE, 0.25F, 1.0F, true);
				}
			}
			if (event.getKeyframeData().getSound().matches("clawSoundkey")) {
				if (this.level.isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
							GigSounds.AQUA_LANDCLAW, SoundSource.HOSTILE, 0.25F, 1.0F, true);
				}
			}
			if (event.getKeyframeData().getSound().matches("idleSoundkey")) {
				if (this.level.isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
							GigSounds.ALIEN_AMBIENT, SoundSource.HOSTILE, 0.25F, 1.0F, true);
				}
			}
		}));
		manager.addController(new AnimationController<>(this, "attackController", 1, event -> {
			if (this.entityData.get(IS_BREAKING) == true && !this.isVehicle()) {
				event.getController().setAnimation(RawAnimation.begin().thenLoop("left_claw"));
				return PlayState.CONTINUE;
			}
			if (getCurrentAttackType() != AlienAttackType.NONE && attackProgress > 0 && !this.isVehicle()
					&& this.isExecuting() == false) {
				event.getController().setAnimation(
						RawAnimation.begin().thenLoop(AlienAttackType.animationMappings.get(getCurrentAttackType())));
				return PlayState.CONTINUE;
			}
			return PlayState.STOP;
		}).setSoundKeyframeHandler(event -> {
			if (event.getKeyframeData().getSound().matches("clawSoundkey")) {
				if (this.level.isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
							GigSounds.ALIEN_CLAW, SoundSource.HOSTILE, 0.25F, 1.0F, true);
				}
			}
			if (event.getKeyframeData().getSound().matches("tailSoundkey")) {
				if (this.level.isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
							GigSounds.ALIEN_TAIL, SoundSource.HOSTILE, 0.25F, 1.0F, true);
				}
			}
		}));
		manager.addController(new AnimationController<>(this, "hissController", 1, event -> {
			var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
			if (this.entityData.get(IS_HISSING) == true && !this.isVehicle() && this.isExecuting() == false
					&& !isDead) {
				event.getController().setAnimation(RawAnimation.begin().thenLoop("hiss"));
				return PlayState.CONTINUE;
			}

			return PlayState.STOP;
		}).setSoundKeyframeHandler(event -> {
			if (event.getKeyframeData().getSound().matches("hissSoundkey")) {
				if (this.level.isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
							GigSounds.ALIEN_HISS, SoundSource.HOSTILE, 1.0F, 1.0F, true);
				}
			}
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}
