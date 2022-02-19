package com.bvanseg.gigeresque.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

import static java.lang.Math.min;

public interface Growable {
    float getGrowth();
    void setGrowth(float growth);
    float getMaxGrowth();

    default void grow(LivingEntity entity, float amount) {
        setGrowth(min(getGrowth() + amount, getMaxGrowth()));

        if (getGrowth() >= getMaxGrowth()) {
            growUp(entity);
        }
    }

    LivingEntity growInto();

    default void growUp(LivingEntity entity) {
        World world = entity.world;
        if (!world.isClient()) {
            var newEntity = growInto();
            if (newEntity == null) return;
                    newEntity.refreshPositionAndAngles(entity.getBlockPos(), entity.getYaw(), entity.getPitch());
            world.spawnEntity(newEntity);
            entity.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    default float getGrowthNeededUntilGrowUp(){
        return getMaxGrowth() - getGrowth();
    }

    default float getGrowthMultiplier() {
        return 1.0f;
    }
}
