package mods.cybercat.gigeresque.common.entity.impl;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;
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
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
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
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class AquaticAlienEntity extends AdultAlienEntity {
	public AquaticAlienEntity(EntityType<? extends AlienEntity> type, World world) {
		super(type, world);
		ignoreCameraFrustum = true;
		stepHeight = 1.0f;

		navigation = swimNavigation;
		moveControl = swimMoveControl;
		lookControl = swimLookControl;
		setPathfindingPenalty(PathNodeType.WATER, 0.0f);
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 90.0)
				.add(EntityAttributes.GENERIC_ARMOR, 4.0).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2500000417232513)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0 * Constants.getIsolationModeDamageBase())
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0)
				.add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE, 0.85);
	}

	private final AnimationFactory animationFactory = new AnimationFactory(this);

	private final MobNavigation landNavigation = new MobNavigation(this, world);
	private final AmphibiousNavigation swimNavigation = new AmphibiousNavigation(this, world);

	private final MoveControl landMoveControl = new MoveControl(this);
	private final LookControl landLookControl = new LookControl(this);
	private final AquaticMoveControl swimMoveControl = new AquaticMoveControl(this, 85, 10, 0.7f, 1.0f, false);
	private final YawAdjustingLookControl swimLookControl = new YawAdjustingLookControl(this, 10);

	@Override
	public float getGrowthMultiplier() {
		return Gigeresque.config.miscellaneous.aquaticAlienGrowthMultiplier;
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

		if (this.isSubmergedInWater()) {
			if (this.isAttacking()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("moving_aggro", true));
				return PlayState.CONTINUE;
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
				return PlayState.CONTINUE;
			}
		} else {
			if (velocityLength > 0.0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("land_moving", true));
				return PlayState.CONTINUE;
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("land_idle", true));
				return PlayState.CONTINUE;
			}
		}
	}

	private <E extends IAnimatable> PlayState hissPredicate(AnimationEvent<E> event) {
		if (isHissing()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("hiss_sound", true));
			return PlayState.CONTINUE;
		}

		return PlayState.STOP;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "controller", 10f, this::predicate));
		data.addAnimationController(new AnimationController<>(this, "hissController", 10f, this::hissPredicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return animationFactory;
	}
}
