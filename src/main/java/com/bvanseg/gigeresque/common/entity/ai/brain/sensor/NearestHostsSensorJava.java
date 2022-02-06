package com.bvanseg.gigeresque.common.entity.ai.brain.sensor;

import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypesJava;
import com.bvanseg.gigeresque.common.util.EntityUtils;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.LivingTargetCache;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.Set;

public class NearestHostsSensorJava extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> getOutputMemoryModules() {
        return ImmutableSet.of(
                MemoryModuleType.NEAREST_ATTACKABLE,
                MemoryModuleType.VISIBLE_MOBS,
                MemoryModuleType.NEAREST_VISIBLE_PLAYER,
                MemoryModuleTypesJava.NEAREST_HOSTS
        );
    }

    @Override
    protected void sense(ServerWorld world, LivingEntity entity) {
        Brain<?> brain = entity.getBrain();

        // Prioritize players
        PlayerEntity nearestPlayer = brain.getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER).orElse(null);
        if (EntityUtils.isPotentialHost(nearestPlayer)) {
            brain.remember(MemoryModuleType.NEAREST_ATTACKABLE, nearestPlayer);
        }

        LivingTargetCache nearestVisibleMobs = brain.getOptionalMemory(MemoryModuleType.VISIBLE_MOBS).orElse(LivingTargetCache.empty());
        List<LivingEntity> nearestHosts = nearestVisibleMobs.stream(EntityUtils::isPotentialHost).toList();

        brain.remember(MemoryModuleTypesJava.NEAREST_HOSTS, nearestHosts);
    }
}
