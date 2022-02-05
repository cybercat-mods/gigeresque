package com.bvanseg.gigeresque.common.entity.ai.brain.task

import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypes
import com.bvanseg.gigeresque.common.entity.impl.AdultAlienEntity
import com.bvanseg.gigeresque.common.extensions.getOrNull
import com.bvanseg.gigeresque.common.extensions.isEggmorphable
import com.google.common.collect.ImmutableMap
import net.minecraft.entity.ai.brain.MemoryModuleState
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.task.Task
import net.minecraft.server.world.ServerWorld

/**
 * @author Boston Vanseghi
 */
class PickUpEggmorphableTargetTask(private val speed: Double): Task<AdultAlienEntity>(
    ImmutableMap.of(
        MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
        MemoryModuleTypes.EGGMORPH_TARGET, MemoryModuleState.VALUE_PRESENT,
        MemoryModuleType.HOME, MemoryModuleState.VALUE_PRESENT
    )
) {

    override fun shouldRun(serverWorld: ServerWorld, alien: AdultAlienEntity): Boolean {
        return alien.brain.hasMemoryModule(MemoryModuleTypes.EGGMORPH_TARGET) && (alien.brain.getOptionalMemory(
            MemoryModuleTypes.EGGMORPH_TARGET).getOrNull()?.let { it.isEggmorphable() } ?: false) && !alien.isCarryingEggmorphableTarget()
    }

    override fun run(serverWorld: ServerWorld, alien: AdultAlienEntity, l: Long) {
        val target = alien.brain.getOptionalMemory(MemoryModuleTypes.EGGMORPH_TARGET).getOrNull() ?: return

        if (alien.distanceTo(target) < 4.0) {
            if (target.vehicle == null) {
                target.startRiding(alien)
            }
        } else {
            alien.navigation.startMovingTo(target.x, target.y, target.z, speed)
        }
    }
}