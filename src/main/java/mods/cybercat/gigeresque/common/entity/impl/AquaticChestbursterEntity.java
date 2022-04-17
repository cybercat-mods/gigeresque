package mods.cybercat.gigeresque.common.entity.impl;

import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.Growable;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
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
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class AquaticChestbursterEntity extends ChestbursterEntity implements IAnimatable, Growable {

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

		if (velocityLength > 0.0 && !this.isAttacking() && !this.isSubmergedInWater()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("slither", true));
			return PlayState.CONTINUE;
		} 
		if (velocityLength > 0.0 && this.isAttacking() && !this.isSubmergedInWater()){
			event.getController().setAnimation(new AnimationBuilder().addAnimation("rush_slither", true));
			return PlayState.CONTINUE;
		} 
		if (this.getTarget() != null && this.tryAttack(getTarget())) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("chomp", true));
			return PlayState.CONTINUE;
		}
		if (this.isDead()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("dead", true));
			return PlayState.CONTINUE;
		}

		if (velocityLength > 0.0 && !this.isAttacking() && this.isSubmergedInWater()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("swim", true));
			return PlayState.CONTINUE;
		} 
		if (velocityLength > 0.0 && this.isAttacking() && this.isSubmergedInWater()){
			event.getController().setAnimation(new AnimationBuilder().addAnimation("rush_swim", true));
			return PlayState.CONTINUE;
		} 
		if (velocityLength == 0.0 && this.isSubmergedInWater()){
			event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_water", true));
			return PlayState.CONTINUE;
		} 
		event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_land", true));
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "controller", 10f, this::predicate));
	}
}
