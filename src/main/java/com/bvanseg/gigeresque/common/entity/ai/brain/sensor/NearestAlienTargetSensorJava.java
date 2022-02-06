package com.bvanseg.gigeresque.common.entity.ai.brain.sensor;

import com.bvanseg.gigeresque.common.config.ConfigAccessorJava;
import com.bvanseg.gigeresque.common.entity.AlienEntityJava;
import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypesJava;
import com.bvanseg.gigeresque.common.util.EntityUtils;
import com.bvanseg.gigeresque.interfacing.Eggmorphable;
import com.bvanseg.gigeresque.interfacing.Host;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.LivingTargetCache;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.Set;

public class NearestAlienTargetSensorJava extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> getOutputMemoryModules() {
        return ImmutableSet.of(
                MemoryModuleTypesJava.EGGMORPH_TARGET,
                MemoryModuleType.NEAREST_ATTACKABLE,
                MemoryModuleType.VISIBLE_MOBS,
                MemoryModuleType.NEAREST_VISIBLE_PLAYER
        );
    }

    @Override
    protected void sense(ServerWorld world, LivingEntity entity) {
        Brain<?> brain = entity.getBrain();

        // Prioritize players
        PlayerEntity nearestPlayer = brain.getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER).orElse(null);
        if (nearestPlayer != null) {
            if (!((Host) nearestPlayer).hasParasite()) {
                brain.remember(MemoryModuleType.NEAREST_ATTACKABLE, nearestPlayer);
                return;
            }
        }

        LivingTargetCache nearestVisibleMobs = brain.getOptionalMemory(MemoryModuleType.VISIBLE_MOBS).orElse(LivingTargetCache.empty());

        nearestVisibleMobs.findFirst(it -> {
            if (entity.getBrain().hasMemoryModule(MemoryModuleType.HOME) && EntityUtils.isEggmorphable(it) && !it.hasVehicle()) {
                brain.remember(MemoryModuleTypesJava.EGGMORPH_TARGET, it);
                return true;
            }
            if (ConfigAccessorJava.isTargetBlacklisted(entity, it)) return false;
            if (ConfigAccessorJava.isTargetWhitelisted(entity, it)) return true;

            return !(it instanceof AlienEntityJava) &&
                    ((Host) it).doesNotHaveParasite() &&
                    ((Eggmorphable) it).isNotEggmorphing() &&
                    !(it instanceof AmbientEntity) && it.getGroup() != EntityGroup.UNDEAD && it.getGroup() != EntityGroup.AQUATIC;
        }).ifPresent(it -> brain.remember(MemoryModuleType.NEAREST_ATTACKABLE, it));

    }
}
