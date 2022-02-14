package com.bvanseg.gigeresque.common.entity.impl;

import com.bvanseg.gigeresque.ConstantsJava;
import com.bvanseg.gigeresque.common.GigeresqueJava;
import com.bvanseg.gigeresque.common.config.ConfigAccessorJava;
import com.bvanseg.gigeresque.common.entity.EntitiesJava;
import com.bvanseg.gigeresque.common.entity.GrowableJava;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class RunnerbursterEntityJava extends ChestbursterEntityJava implements IAnimatable, GrowableJava {
    public RunnerbursterEntityJava(EntityType<? extends RunnerbursterEntityJava> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 15.0)
                .add(EntityAttributes.GENERIC_ARMOR, 2.0)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23000000417232513)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.3);
    }

    private AnimationFactory animationFactory = new AnimationFactory(this);

    @Override
    protected int getAcidDiameter() {
        return 1;
    }

    /*
     * GROWTH
     */

    @Override
    public float getGrowthMultiplier() {
        return GigeresqueJava.config.miscellaneous.runnerbursterGrowthMultiplier;
    }

    @Override
    public float getMaxGrowth() {
        return ConstantsJava.TPD / 2.0f;
    }

    @Override
    public LivingEntity growInto() {
        if (hostId == null) {
            return new ClassicAlienEntityJava(EntitiesJava.ALIEN, world);
        }

        var variantId = ConfigAccessorJava.getReversedMorphMappings().get(hostId);
        if (variantId == null) {
            return new ClassicAlienEntityJava(EntitiesJava.ALIEN, world);
        }
        var identifier = new Identifier(variantId);
        var entityType = Registry.ENTITY_TYPE.getOrEmpty(identifier).orElse(null);
        if (entityType == null) {
            return new ClassicAlienEntityJava(EntitiesJava.ALIEN, world);
        }
        var entity = entityType.create(world);

        if (hasCustomName()) {
            if (entity != null) {
                entity.setCustomName(getCustomName());
            }
        }

        return (LivingEntity) entity;
    }

    /*
     * ANIMATIONS
     */

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        var velocityLength = this.getVelocity().horizontalLength();

        if (velocityLength > 0.0 && !this.isTouchingWater()) {
            if (this.isAttacking()) {
                event.getController().setAnimation(
                        new AnimationBuilder()
                                .addAnimation("moving_aggro", true)
                );
                return PlayState.CONTINUE;
            } else {
                event.getController().setAnimation(
                        new AnimationBuilder().addAnimation("moving_noaggro", true)
                );
                return PlayState.CONTINUE;
            }
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
            return PlayState.CONTINUE;
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 10f, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return animationFactory;
    }
}
