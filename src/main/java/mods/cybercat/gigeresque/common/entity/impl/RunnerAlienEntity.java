package mods.cybercat.gigeresque.common.entity.impl;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.data.handler.TrackedDataHandlers;
import mods.cybercat.gigeresque.common.entity.AlienAttackType;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class RunnerAlienEntity extends AdultAlienEntity {
	public RunnerAlienEntity(EntityType<? extends AlienEntity> type, World world) {
		super(type, world);
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 80.0)
				.add(EntityAttributes.GENERIC_ARMOR, 4.0).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23000000417232513)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0 * Constants.getIsolationModeDamageBase())
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0)
				.add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE, 0.5);
	}

	private static final TrackedData<AlienAttackType> CURRENT_ATTACK_TYPE = DataTracker
			.registerData(RunnerAlienEntity.class, TrackedDataHandlers.ALIEN_ATTACK_TYPE);

	private final AnimationFactory animationFactory = new AnimationFactory(this);

	private AlienAttackType getCurrentAttackType() {
		return dataTracker.get(CURRENT_ATTACK_TYPE);
	}

	private void setCurrentAttackType(AlienAttackType value) {
		dataTracker.set(CURRENT_ATTACK_TYPE, value);
	}

	private int attackProgress = 0;

	@Override
	public void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(CURRENT_ATTACK_TYPE, AlienAttackType.NONE);
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
			setCurrentAttackType(switch (random.nextInt(6)) {
			case 0 -> AlienAttackType.CLAW_LEFT;
			case 1 -> AlienAttackType.CLAW_RIGHT;
			case 2 -> AlienAttackType.TAIL_LEFT;
			case 3 -> AlienAttackType.TAIL_RIGHT;
			case 4 -> AlienAttackType.TAIL_OVER;
			case 5 -> AlienAttackType.HEAD_BITE;
			default -> AlienAttackType.CLAW_LEFT;
			});
		}
	}

	@Override
	public float getGrowthMultiplier() {
		return Gigeresque.config.miscellaneous.runnerAlienGrowthMultiplier;
	}

	/*
	 * ANIMATIONS
	 */

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		var velocityLength = this.getVelocity().horizontalLength();

		if (velocityLength > 0.0 && !this.isTouchingWater()) {
			if (this.isAttacking()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("moving_aggro", true)
//                        .addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()), true)
				);
				return PlayState.CONTINUE;
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("moving_noaggro", true)
//                        .addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()), true)
				);
				return PlayState.CONTINUE;
			}
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
			return PlayState.CONTINUE;
		}
	}

	private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
		if (getCurrentAttackType() != AlienAttackType.NONE && attackProgress > 0) {
			event.getController().setAnimation(new AnimationBuilder()
//                        .addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()), true)
			);
			return PlayState.CONTINUE;
		}

		return PlayState.STOP;
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
		data.addAnimationController(new AnimationController<>(this, "attackController", 0f, this::attackPredicate));
		data.addAnimationController(new AnimationController<>(this, "hissController", 10f, this::hissPredicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return animationFactory;
	}
}
