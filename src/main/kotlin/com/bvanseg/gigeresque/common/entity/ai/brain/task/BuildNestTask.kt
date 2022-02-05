package com.bvanseg.gigeresque.common.entity.ai.brain.task

import com.bvanseg.gigeresque.common.entity.AlienEntity
import com.bvanseg.gigeresque.common.extensions.getOrNull
import com.bvanseg.gigeresque.common.util.nest.NestBuildingHelper
import com.google.common.collect.ImmutableMap
import net.minecraft.entity.ai.brain.MemoryModuleState
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.task.Task
import net.minecraft.server.world.ServerWorld
import kotlin.math.max

/**
 * @author Boston Vanseghi
 */
class BuildNestTask: Task<AlienEntity>(
    ImmutableMap.of(
        MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
        MemoryModuleType.HOME, MemoryModuleState.VALUE_PRESENT
    )
) {
    private var cooldown = 0

    override fun shouldRun(serverWorld: ServerWorld, alien: AlienEntity): Boolean {
        val homePos = alien.brain.getOptionalMemory(MemoryModuleType.HOME).getOrNull() ?: return false
        val isWithinNestRange = homePos.pos.getManhattanDistance(alien.blockPos) < 50
        cooldown = max(cooldown - 1, 0)
        return cooldown <= 0 && isWithinNestRange
    }

    override fun run(serverWorld: ServerWorld, alien: AlienEntity, l: Long) {
        NestBuildingHelper.tryBuildNestAround(alien)
        cooldown += 180
    }
}