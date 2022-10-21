package mods.cybercat.gigeresque.common.entity.impl;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.enums.AlienAttackType;
import mods.cybercat.gigeresque.common.entity.ai.goal.AlienMeleeAttackGoal;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;
import mods.cybercat.gigeresque.common.sound.GigSounds;
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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class AquaticAlienEntity extends AdultAlienEntity {

	private final AnimationFactory animationFactory = new AnimationFactory(this);
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
				.add(Attributes.ATTACK_DAMAGE, 7.0 * Constants.getIsolationModeDamageBase())
				.add(Attributes.ATTACK_KNOCKBACK, 1.0).add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE, 0.85);
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
			}
		}

		target.hurt(DamageSource.mobAttack(this), additionalDamage);

		return super.doHurtTarget(target);
	}

	/*
	 * ANIMATIONS
	 */

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		var velocityLength = this.getDeltaMovement().horizontalDistance();
		var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
		if (this.isUnderWater() && this.wasTouchingWater) {
			if (this.isAggressive() && velocityLength > 0.0 && !isDead && this.isStatis() == false) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("rush_swim", true));
				return PlayState.CONTINUE;
			} else if (!this.isAggressive() && velocityLength > 0.0 && !isDead && this.isStatis() == false) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("swim", true));
				return PlayState.CONTINUE;
			} else if (isDead) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("death", true));
				return PlayState.CONTINUE;
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_water", true));
				return PlayState.CONTINUE;
			}
		} else {
			if (this.isAggressive() && velocityLength > 0.0 && !isDead && this.isStatis() == false) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("rush_crawl", true));
				return PlayState.CONTINUE;
			} else if (!this.isAggressive() && velocityLength > 0.0 && !isDead && this.isStatis() == false) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("crawl", true));
				return PlayState.CONTINUE;
			} else if (isSearching && !this.isAggressive() && !isDead && this.isStatis() == false) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("ambient", false));
				return PlayState.CONTINUE;
			} else if (isDead) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("death", true));
				return PlayState.CONTINUE;
			} else if (this.isStatis() == true || this.isNoAi()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("stasis", true));
				return PlayState.CONTINUE;
			} else if (this.isStatis() == false) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_land2", true));
				return PlayState.CONTINUE;
			}
			return PlayState.CONTINUE;
		}
	}

	private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
		if (getCurrentAttackType() != AlienAttackType.NONE && attackProgress > 0) {
			event.getController().setAnimation(new AnimationBuilder()
					.addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()), true));
			return PlayState.CONTINUE;
		}
		if (this.entityData.get(IS_BREAKING) == true) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("left_claw", true));
			return PlayState.CONTINUE;
		}

		return PlayState.STOP;
	}

	private <E extends IAnimatable> PlayState hissPredicate(AnimationEvent<E> event) {
		if (this.entityData.get(IS_HISSING) == true) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("hiss", true));
			return PlayState.CONTINUE;
		}

		return PlayState.STOP;
	}

	private <ENTITY extends IAnimatable> void soundStepListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("stepSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
						GigSounds.AQUA_LANDMOVE, SoundSource.HOSTILE, 0.25F, 1.0F, true);
			}
		}
		if (event.sound.matches("clawSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
						GigSounds.AQUA_LANDCLAW, SoundSource.HOSTILE, 0.25F, 1.0F, true);
			}
		}
		if (event.sound.matches("idleSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
						GigSounds.ALIEN_AMBIENT, SoundSource.HOSTILE, 0.25F, 1.0F, true);
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
						SoundSource.HOSTILE, 0.25F, 1.0F, true);
			}
		}
	}

	@Override
	public void registerControllers(AnimationData data) {
		AnimationController<AquaticAlienEntity> main = new AnimationController<AquaticAlienEntity>(this, "controller",
				10f, this::predicate);
		main.registerSoundListener(this::soundStepListener);
		data.addAnimationController(main);
		AnimationController<AquaticAlienEntity> attacking = new AnimationController<AquaticAlienEntity>(this,
				"attackController", 5f, this::attackPredicate);
		attacking.registerSoundListener(this::soundAttackListener);
		data.addAnimationController(attacking);
		AnimationController<AquaticAlienEntity> hissing = new AnimationController<AquaticAlienEntity>(this,
				"hissController", 10f, this::hissPredicate);
		hissing.registerSoundListener(this::soundHissListener);
		data.addAnimationController(hissing);
	}

	@Override
	public AnimationFactory getFactory() {
		return animationFactory;
	}
}
