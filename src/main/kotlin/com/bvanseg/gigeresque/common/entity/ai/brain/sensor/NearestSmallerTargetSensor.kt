package com.bvanseg.gigeresque.common.entity.ai.brain.sensor

import com.bvanseg.gigeresque.common.entity.AlienEntity
import com.google.common.collect.ImmutableSet
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.sensor.Sensor
import net.minecraft.server.world.ServerWorld

/**
 * @author Boston Vanseghi
 */
class NearestSmallerTargetSensor : Sensor<LivingEntity>() {

    override fun getOutputMemoryModules(): Set<MemoryModuleType<*>> {
        return ImmutableSet.of(
            MemoryModuleType.ATTACK_TARGET, MemoryModuleType.VISIBLE_MOBS
        )
    }

    override fun sense(world: ServerWorld, entity: LivingEntity) {
        val brain = entity.brain

        val nearestVisibleMobs = brain.getOptionalMemory(MemoryModuleType.VISIBLE_MOBS).orElse(listOf())

        nearestVisibleMobs.firstOrNull {
            if (it is AlienEntity) return@firstOrNull false
            it.height * it.width < (entity.height * entity.width) * 3
        }?.let {
            brain.remember(MemoryModuleType.ATTACK_TARGET, it)
        }
    }
}