package mods.cybercat.gigeresque.common.entity.impl;

import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
import mods.cybercat.gigeresque.common.entity.helper.Growable;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class AquaticChestbursterEntity extends ChestbursterEntity implements IAnimatable, Growable, IAnimationTickable {

	private final GroundPathNavigation landNavigation = new GroundPathNavigation(this, level);
	private final AmphibiousNavigation swimNavigation = new AmphibiousNavigation(this, level);
	private final MoveControl landMoveControl = new MoveControl(this);
	private final LookControl landLookControl = new LookControl(this);
	private final SmoothSwimmingMoveControl swimMoveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.7f, 1.0f,
			false);
	private final SmoothSwimmingLookControl swimLookControl = new SmoothSwimmingLookControl(this, 10);

	public AquaticChestbursterEntity(EntityType<? extends AquaticChestbursterEntity> type, Level world) {
		super(type, world);
		maxUpStep = 1.0f;

		navigation = swimNavigation;
		moveControl = swimMoveControl;
		lookControl = swimLookControl;
		setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
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
			setDeltaMovement(getDeltaMovement().scale(0.9));
			if (getTarget() == null) {
				setDeltaMovement(getDeltaMovement().add(0.0, -0.005, 0.0));
			}
		} else {
			super.travel(movementInput);
		}
	}

	@Override
	public LivingEntity growInto() {
		var entity = new AquaticAlienEntity(Entities.AQUATIC_ALIEN, level);

		if (hasCustomName()) {
			entity.setCustomName(this.getCustomName());
		}

		return entity;
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

	/*
	 * ANIMATIONS
	 */

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		var velocityLength = this.getDeltaMovement().horizontalDistance();
		var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
		if (velocityLength >= 0.000000001 && !isDead && animationSpeedOld > 0.15F) {
			if (this.isUnderWater()) {
				if (animationSpeedOld >= 0.35F) {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("rush_swim", true));
					return PlayState.CONTINUE;
				} else {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("swim", true));
					return PlayState.CONTINUE;
				}
			} else {
				if (animationSpeedOld >= 0.35F) {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("rush_slither", true));
					return PlayState.CONTINUE;
				} else {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("slither", true));
					return PlayState.CONTINUE;
				}
			}
		} else if (this.entityData.get(EAT) == true && !this.isDeadOrDying()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("chomp", false));
			return PlayState.CONTINUE;
		} else if (isDead) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("death", true));
			return PlayState.CONTINUE;
		} else {
			if (this.tickCount < 5 && this.entityData.get(BIRTHED) == true) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("birth", true));
				return PlayState.CONTINUE;
			} else {
				if (this.isUnderWater()) {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_water", true));
					return PlayState.CONTINUE;
				} else {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_land", true));
					return PlayState.CONTINUE;
				}
			}
		}
	}

	private <ENTITY extends IAnimatable> void soundListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("stepSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
						GigSounds.BURSTER_CRAWL, SoundSource.HOSTILE, 0.25F, 1.0F, true);
			}
		}
	}

	@Override
	public void registerControllers(AnimationData data) {
		AnimationController<AquaticChestbursterEntity> controller = new AnimationController<AquaticChestbursterEntity>(
				this, "controller", 10f, this::predicate);
		controller.registerSoundListener(this::soundListener);
		data.addAnimationController(controller);
	}

	@Override
	public int tickTimer() {
		return tickCount;
	}
}
