package mods.cybercat.gigeresque.common.entity.helper;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerbursterEntity;

import static java.lang.Math.min;

public interface Growable {

    float getGrowth();

    void setGrowth(float growth);

    float getMaxGrowth();

    default void grow(Entity entity, float amount) {
        setGrowth(min(getGrowth() + amount, getMaxGrowth()));
        if (getGrowth() >= getMaxGrowth())
            growUp(entity);
    }

    LivingEntity growInto();

    default void growUp(Entity entity) {
        var world = entity.level();
        if (!world.isClientSide()) {
            var newEntity = growInto();
            if (newEntity == null)
                return;
            newEntity.moveTo(entity.blockPosition(), entity.getYRot(), entity.getXRot());
            if (newEntity instanceof RunnerbursterEntity runnerBurster)
                runnerBurster.setBirthStatus(false);
            world.addFreshEntity(newEntity);
            if (entity.hasCustomName())
                newEntity.setCustomName(entity.getCustomName());
            if (entity instanceof LivingEntity livingEntity && newEntity instanceof LivingEntity newLivingEntity) {
                for (var effect : livingEntity.getActiveEffects()) {
                    newLivingEntity.addEffect(new MobEffectInstance(effect));
                }
            }
            entity.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    default float getGrowthNeededUntilGrowUp() {
        return getMaxGrowth() - getGrowth();
    }

    default float getGrowthMultiplier() {
        return 1.0f;
    }
}
