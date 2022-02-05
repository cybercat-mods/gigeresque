package com.bvanseg.gigeresque.common.entity.ai.brain.sensor

import com.bvanseg.gigeresque.common.extensions.isPotentialHost
import com.google.common.collect.ImmutableSet
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.sensor.Sensor
import net.minecraft.server.world.ServerWorld

/**
 * @author Boston Vanseghi
 */
class NearestHostSensor : Sensor<LivingEntity>() {

    override fun getOutputMemoryModules(): Set<MemoryModuleType<*>> {
        return ImmutableSet.of(
            MemoryModuleType.ATTACK_TARGET, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.NEAREST_VISIBLE_PLAYER
        )
    }

    override fun sense(world: ServerWorld, entity: LivingEntity) {
        val brain = entity.brain

        // Prioritize players
        val nearestPlayer = brain.getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER)

        if (nearestPlayer.isPresent && nearestPlayer.get().isPotentialHost()) {
            brain.remember(MemoryModuleType.ATTACK_TARGET, nearestPlayer.get())
            return
        }

        val nearestVisibleMobs = brain.getOptionalMemory(MemoryModuleType.VISIBLE_MOBS).orElse(listOf())

        nearestVisibleMobs.firstOrNull {
            it.isPotentialHost()
        }?.let {
            brain.remember(MemoryModuleType.ATTACK_TARGET, it)
        }
    }
}