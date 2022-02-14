package com.bvanseg.gigeresque.common.entity.impl;

import com.bvanseg.gigeresque.ConstantsJava;
import com.bvanseg.gigeresque.common.GigeresqueJava;
import com.bvanseg.gigeresque.common.data.handler.TrackedDataHandlersJava;
import com.bvanseg.gigeresque.common.entity.AlienAttackTypeJava;
import com.bvanseg.gigeresque.common.entity.AlienEntityJava;
import com.bvanseg.gigeresque.common.entity.attribute.AlienEntityAttributesJava;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class RunnerAlienEntityJava extends AdultAlienEntityJava {
    public RunnerAlienEntityJava(EntityType<? extends AlienEntityJava> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 80.0)
                .add(EntityAttributes.GENERIC_ARMOR, 4.0)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23000000417232513)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0 * ConstantsJava.getIsolationModeDamageBase())
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0)
                .add(AlienEntityAttributesJava.INTELLIGENCE_ATTRIBUTE, 0.5);
    }

    private static final TrackedData<AlienAttackTypeJava> CURRENT_ATTACK_TYPE = DataTracker.registerData(RunnerAlienEntity.class, TrackedDataHandlersJava.ALIEN_ATTACK_TYPE);

    private final AnimationFactory animationFactory = new AnimationFactory(this);

    private AlienAttackTypeJava getCurrentAttackType() {
        return dataTracker.get(CURRENT_ATTACK_TYPE);
    }

    private void setCurrentAttackType(AlienAttackTypeJava value) {
        dataTracker.set(CURRENT_ATTACK_TYPE, value);
    }

    private int attackProgress = 0;

    @Override
    public void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(CURRENT_ATTACK_TYPE, AlienAttackTypeJava.NONE);
    }

    @Override
    public void tick() {
        super.tick();

        // Attack logic

        if (attackProgress > 0) {
            attackProgress--;

            if (!world.isClient && attackProgress <= 0) {
                setCurrentAttackType(AlienAttackTypeJava.NONE);
            }
        }

        if (attackProgress == 0 && handSwinging) {
            attackProgress = 10;
        }

        if (!world.isClient && getCurrentAttackType() == AlienAttackTypeJava.NONE) {
            setCurrentAttackType(switch (random.nextInt(6)) {
                case 0 -> AlienAttackTypeJava.CLAW_LEFT;
                case 1 -> AlienAttackTypeJava.CLAW_RIGHT;
                case 2 -> AlienAttackTypeJava.TAIL_LEFT;
                case 3 -> AlienAttackTypeJava.TAIL_RIGHT;
                case 4 -> AlienAttackTypeJava.TAIL_OVER;
                case 5 -> AlienAttackTypeJava.HEAD_BITE;
                default -> AlienAttackTypeJava.CLAW_LEFT;
            });
        }
    }

    @Override
    public float getGrowthMultiplier() {
        return GigeresqueJava.config.miscellaneous.runnerAlienGrowthMultiplier;
    }

    /*
        ANIMATIONS
     */

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        var velocityLength = this.getVelocity().horizontalLength();

        if (velocityLength > 0.0 && !this.isTouchingWater()) {
            if (this.isAttacking()) {
                event.getController().setAnimation(
                        new AnimationBuilder()
                                .addAnimation("moving_aggro", true)
//                        .addAnimation(AlienAttackTypeJava.animationMappings.get(getCurrentAttackType()), true)
                );
                return PlayState.CONTINUE;
            } else {
                event.getController().setAnimation(
                        new AnimationBuilder()
                                .addAnimation("moving_noaggro", true)
//                        .addAnimation(AlienAttackTypeJava.animationMappings.get(getCurrentAttackType()), true)
                );
                return PlayState.CONTINUE;
            }
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
            return PlayState.CONTINUE;
        }
    }

    private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
        if (getCurrentAttackType() != AlienAttackTypeJava.NONE && attackProgress > 0) {
            event.getController().setAnimation(new AnimationBuilder()
//                        .addAnimation(AlienAttackTypeJava.animationMappings.get(getCurrentAttackType()), true)
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
