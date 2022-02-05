package com.bvanseg.gigeresque.common.entity.ai.brain.task

import com.bvanseg.gigeresque.Constants
import com.bvanseg.gigeresque.common.block.Blocks
import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypes
import com.bvanseg.gigeresque.common.entity.impl.AdultAlienEntity
import com.bvanseg.gigeresque.common.extensions.getOrNull
import com.bvanseg.gigeresque.interfacing.Eggmorphable
import com.google.common.collect.ImmutableMap
import net.minecraft.entity.ai.brain.MemoryModuleState
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.task.Task
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d

/**
 * @author Boston Vanseghi
 */
class EggmorphTargetTask(private val speed: Double): Task<AdultAlienEntity>(
    ImmutableMap.of(
        MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
        MemoryModuleTypes.NEAREST_ALIEN_WEBBING, MemoryModuleState.REGISTERED,
        MemoryModuleType.HOME, MemoryModuleState.VALUE_PRESENT
    )
) {

    override fun shouldRun(serverWorld: ServerWorld, alien: AdultAlienEntity): Boolean {
        return alien.isCarryingEggmorphableTarget()
    }

    override fun run(serverWorld: ServerWorld, alien: AdultAlienEntity, l: Long) {
        val target = alien.firstPassenger ?: return
        val home = alien.brain.getOptionalMemory(MemoryModuleType.HOME).getOrNull()?.pos ?: return

        if (alien.blockPos.getManhattanDistance(home) < 32 &&
            alien.brain.hasMemoryModule(MemoryModuleTypes.NEAREST_ALIEN_WEBBING)) {
            val nearestWebbing = alien.brain.getOptionalMemory(MemoryModuleTypes.NEAREST_ALIEN_WEBBING).getOrNull() ?: return

            if (alien.blockPos.getManhattanDistance(nearestWebbing) < 4) {
                (target as Eggmorphable).ticksUntilEggmorphed = Constants.TPD
                target.stopRiding()
                target.setPosition(Vec3d.ofBottomCenter(nearestWebbing))

                var hasCeiling = false

                var ceilingUp = nearestWebbing.up()
                for (i in 0 until 20) {
                    if (alien.world.getBlockState(ceilingUp).isOpaqueFullCube(alien.world, ceilingUp)) {
                        hasCeiling = true
                        break
                    }
                    ceilingUp = ceilingUp.up()
                }

                var up = nearestWebbing.up()
                if (hasCeiling) {
                    for (i in 0 until 20) {
                        val state = alien.world.getBlockState(up)

                        if (state.isAir) {
                            alien.world.setBlockState(up, Blocks.NEST_RESIN_WEB_CROSS.defaultState)
                        } else {
                            break
                        }
                        up = up.up()
                    }
                }

            } else {
                alien.navigation.startMovingTo(nearestWebbing.x.toDouble(), nearestWebbing.y.toDouble(), nearestWebbing.z.toDouble(), speed)
            }

        } else {
            alien.navigation.startMovingTo(home.x.toDouble(), home.y.toDouble(), home.z.toDouble(), speed)
        }
    }
}