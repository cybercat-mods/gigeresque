package com.bvanseg.gigeresque.common.entity.ai.brain.sensor

import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypes
import com.bvanseg.gigeresque.common.entity.impl.FacehuggerEntity
import com.google.common.collect.ImmutableSet
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.LivingTargetCache
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.sensor.Sensor
import net.minecraft.server.world.ServerWorld

/**
 * @author Boston Vanseghi
 */
class NearestFacehuggersSensor : Sensor<LivingEntity>() {

    override fun getOutputMemoryModules(): Set<MemoryModuleType<*>> {
        return ImmutableSet.of(
            MemoryModuleTypes.NEAREST_FACEHUGGERS
        )
    }

    override fun sense(world: ServerWorld, entity: LivingEntity) {
        val brain = entity.brain
        val nearestVisibleMobs =
            brain.getOptionalMemory(MemoryModuleType.VISIBLE_MOBS).orElse(LivingTargetCache.empty())

        @Suppress("UNCHECKED_CAST")
        val nearestFacehuggers =
            nearestVisibleMobs.stream { it is FacehuggerEntity }.toList() as MutableList<FacehuggerEntity>

        brain.remember(MemoryModuleTypes.NEAREST_FACEHUGGERS, nearestFacehuggers)
    }
}