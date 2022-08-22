package mods.cybercat.gigeresque.common.entity.impl;

import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.Growable;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.control.YawAdjustingLookControl;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.fluid.Fluid;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class AquaticChestbursterEntity extends ChestbursterEntity implements IAnimatable, Growable, IAnimationTickable {

	private final MobNavigation landNavigation = new MobNavigation(this, world);
	private final AmphibiousNavigation swimNavigation = new AmphibiousNavigation(this, world);
	private final MoveControl landMoveControl = new MoveControl(this);
	private final LookControl landLookControl = new LookControl(this);
	private final AquaticMoveControl swimMoveControl = new AquaticMoveControl(this, 85, 10, 0.7f, 1.0f, false);
	private final YawAdjustingLookControl swimLookControl = new YawAdjustingLookControl(this, 10);

	public AquaticChestbursterEntity(EntityType<? extends AquaticChestbursterEntity> type, World world) {
		super(type, world);
		ignoreCameraFrustum = true;
		stepHeight = 1.0f;

		navigation = swimNavigation;
		moveControl = swimMoveControl;
		lookControl = swimLookControl;
		setPathfindingPenalty(PathNodeType.WATER, 0.0f);
	}

	@Override
	public void travel(Vec3d movementInput) {
		this.navigation = (this.isSubmergedInWater() || this.isTouchingWater()) ? swimNavigation : landNavigation;
		this.moveControl = (this.submergedInWater || this.isTouchingWater()) ? swimMoveControl : landMoveControl;
		this.lookControl = (this.submergedInWater || this.isTouchingWater()) ? swimLookControl : landLookControl;

		if (this.age % 10 == 0) {
			this.calculateDimensions();
		}

		if (canMoveVoluntarily() && this.isTouchingWater()) {
			updateVelocity(getMovementSpeed(), movementInput);
			move(MovementType.SELF, getVelocity());
			setVelocity(getVelocity().multiply(0.9));
			if (getTarget() == null) {
				setVelocity(getVelocity().add(0.0, -0.005, 0.0));
			}
		} else {
			super.travel(movementInput);
		}
	}

	@Override
	public LivingEntity growInto() {
		var entity = new AquaticAlienEntity(Entities.AQUATIC_ALIEN, world);

		if (hasCustomName()) {
			entity.setCustomName(this.getCustomName());
		}

		return entity;
	}

	@Override
	public boolean canBreatheInWater() {
		return true;
	}

	@Override
	public EntityNavigation createNavigation(World world) {
		return swimNavigation;
	}

	@Override
	public boolean isPushedByFluids() {
		return false;
	}

	@Override
	protected void swimUpward(TagKey<Fluid> fluid) {
	}

	@Override
	public EntityDimensions getDimensions(EntityPose pose) {
		return this.submergedInWater ? super.getDimensions(pose).scaled(1.0f, 0.5f) : super.getDimensions(pose);
	}

	/*
	 * ANIMATIONS
	 */

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		var velocityLength = this.getVelocity().horizontalLength();
		var isDead = this.dead || this.getHealth() < 0.01 || this.isDead();
		if (velocityLength >= 0.000000001 && !isDead && lastLimbDistance > 0.15F) {
			if (this.isSubmergedInWater()) {
				if (lastLimbDistance >= 0.35F) {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("rush_swim", true));
					return PlayState.CONTINUE;
				} else {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("swim", true));
					return PlayState.CONTINUE;
				}
			} else {
				if (lastLimbDistance >= 0.35F) {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("rush_slither", true));
					return PlayState.CONTINUE;
				} else {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("slither", true));
					return PlayState.CONTINUE;
				}
			}
		} else if (this.dataTracker.get(EAT) == true && !this.isDead()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("chomp", false));
			return PlayState.CONTINUE;
		} else if (isDead) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("death", true));
			return PlayState.CONTINUE;
		} else {
			if (this.age < 5 && this.dataTracker.get(BIRTHED) == true) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("birth", true));
				return PlayState.CONTINUE;
			} else {
				if (this.isSubmergedInWater()) {
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
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.BURSTER_CRAWL,
						SoundCategory.HOSTILE, 0.25F, 1.0F, true);
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
		return age;
	}
}
