package com.bvanseg.gigeresque.common.entity.ai.brain.task

import com.bvanseg.gigeresque.common.entity.impl.AdultAlienEntity
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
class BuildNestTask : Task<AdultAlienEntity>(
    ImmutableMap.of(
        MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
        MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_ABSENT,
        MemoryModuleType.HOME, MemoryModuleState.VALUE_PRESENT
    )
) {
    private var cooldown = 0

    override fun shouldRun(serverWorld: ServerWorld, alien: AdultAlienEntity): Boolean {
        val homePos = alien.brain.getOptionalMemory(MemoryModuleType.HOME).getOrNull() ?: return false
        val isWithinNestRange = homePos.pos.getManhattanDistance(alien.blockPos) < 50
        cooldown = max(cooldown - 1, 0)
        return alien.growth == alien.maxGrowth &&
                cooldown <= 0 && isWithinNestRange && !alien.world.isSkyVisible(alien.blockPos)
    }

    override fun run(serverWorld: ServerWorld, alien: AdultAlienEntity, l: Long) {
        NestBuildingHelper.tryBuildNestAround(alien)
        cooldown += 180
    }
}