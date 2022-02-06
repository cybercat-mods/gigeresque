package com.bvanseg.gigeresque.common.entity.ai.brain.sensor;

import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypesJava;
import com.bvanseg.gigeresque.common.entity.impl.AlienEggEntity;
import com.bvanseg.gigeresque.common.entity.impl.AlienEggEntityJava;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.LivingTargetCache;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.Set;

public class NearestEggsSensorJava extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> getOutputMemoryModules() {
        return ImmutableSet.of(
                MemoryModuleTypesJava.NEAREST_EGGS
        );
    }

    @Override
    protected void sense(ServerWorld world, LivingEntity entity) {
        Brain<?> brain = entity.getBrain();
        LivingTargetCache nearestVisibleMobs = brain.getOptionalMemory(MemoryModuleType.VISIBLE_MOBS).orElse(LivingTargetCache.empty());
        List<AlienEggEntityJava> nearestEggs = nearestVisibleMobs.stream(it -> it instanceof AlienEggEntityJava).map(it -> (AlienEggEntityJava) it).toList();
        brain.remember(MemoryModuleTypesJava.NEAREST_EGGS, nearestEggs);
    }
}
