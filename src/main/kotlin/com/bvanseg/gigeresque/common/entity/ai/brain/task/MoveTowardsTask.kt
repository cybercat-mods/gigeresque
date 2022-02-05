package com.bvanseg.gigeresque.common.entity.ai.brain.task

import com.google.common.collect.ImmutableMap
import net.minecraft.entity.ai.NoPenaltyTargeting
import net.minecraft.entity.ai.brain.Brain
import net.minecraft.entity.ai.brain.MemoryModuleState
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.WalkTarget
import net.minecraft.entity.ai.brain.task.Task
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.dynamic.GlobalPos
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

/**
 * Repurposed from VillagerWalkTowardsTask. This is the specific AI class used for when the villager navigates long
 * distances back to its meeting point or work site.
 *
 * This code was duplicated due to the original AI class being VillagerEntity-specific.
 *
 * @author Boston Vanseghi
 */
class MoveTowardsTask(
    private val destination: MemoryModuleType<GlobalPos>,
    private val speed: Float,
    private val completionRange: Int,
    private val maxRange: Int,
    private val maxRunTime: Int
) :
    Task<PathAwareEntity>(
        ImmutableMap.of(
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleState.REGISTERED,
            MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT,
            destination, MemoryModuleState.VALUE_PRESENT
        )
    ) {
    private fun giveUp(alien: PathAwareEntity, time: Long) {
        val brain: Brain<*> = alien.brain
        brain.forget(destination)
        brain.remember(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, time)
    }

    override fun run(serverWorld: ServerWorld, pathAwareEntity: PathAwareEntity, l: Long) {
        val brain: Brain<*> = pathAwareEntity.brain
        brain.getOptionalMemory(destination).ifPresent { globalPos: GlobalPos ->
            if (!dimensionMismatches(serverWorld, globalPos) && !shouldGiveUp(serverWorld, pathAwareEntity)) {
                if (exceedsMaxRange(pathAwareEntity, globalPos)) {
                    var vec3d: Vec3d? = null
                    var i = 0
                    while (i < 1000 && (vec3d == null || exceedsMaxRange(
                            pathAwareEntity,
                            GlobalPos.create(serverWorld.registryKey, BlockPos(vec3d))
                        ))
                    ) {
                        vec3d = NoPenaltyTargeting.find(
                            pathAwareEntity,
                            15,
                            7,
                            // TODO: What is the drawback of excluding these parameters?
//                            Vec3d.ofBottomCenter(globalPos.pos),
//                            1.5707963705062866
                        )
                        ++i
                    }
                    if (i == 1000) {
                        giveUp(pathAwareEntity, l)
                        return@ifPresent
                    }
                    brain.remember(
                        MemoryModuleType.WALK_TARGET,
                        WalkTarget(vec3d, speed, completionRange)
                    )
                } else if (!reachedDestination(serverWorld, pathAwareEntity, globalPos)) {
                    brain.remember(
                        MemoryModuleType.WALK_TARGET,
                        WalkTarget(globalPos.pos, speed, completionRange)
                    )
                }
            } else {
                giveUp(pathAwareEntity, l)
            }
        }
    }

    private fun shouldGiveUp(world: ServerWorld, alien: PathAwareEntity): Boolean {
        val optional = alien.brain.getOptionalMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)
        return if (optional.isPresent) {
            world.time - optional.get() > this.maxRunTime.toLong()
        } else {
            false
        }
    }

    private fun exceedsMaxRange(alien: PathAwareEntity, pos: GlobalPos): Boolean {
        return pos.pos.getManhattanDistance(alien.blockPos) > maxRange
    }

    private fun dimensionMismatches(world: ServerWorld, pos: GlobalPos): Boolean {
        return pos.dimension !== world.registryKey
    }

    private fun reachedDestination(world: ServerWorld, alien: PathAwareEntity, pos: GlobalPos): Boolean {
        return pos.dimension === world.registryKey && pos.pos.getManhattanDistance(alien.blockPos) <= completionRange
    }
}