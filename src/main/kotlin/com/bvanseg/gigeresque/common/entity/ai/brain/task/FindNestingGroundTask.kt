package com.bvanseg.gigeresque.common.entity.ai.brain.task

import com.bvanseg.gigeresque.common.block.Blocks
import com.bvanseg.gigeresque.common.block.NestResinBlock
import com.bvanseg.gigeresque.common.entity.AlienEntity
import com.google.common.collect.ImmutableMap
import net.minecraft.entity.ai.brain.MemoryModuleState
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.task.Task
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.dynamic.GlobalPos
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.LightType
import java.util.*

/**
 * @author Boston Vanseghi
 */
class FindNestingGroundTask(private val speed: Double): Task<AlienEntity>(
    ImmutableMap.of(
        MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
        MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT,
        MemoryModuleType.HOME, MemoryModuleState.VALUE_ABSENT
    )
) {
    var hasReachedNestingGround = false
    var targetPos: BlockPos? = null

    override fun shouldRun(serverWorld: ServerWorld, alien: AlienEntity): Boolean {
        return !alien.brain.hasMemoryModule(MemoryModuleType.HOME) || !hasReachedNestingGround
    }

    override fun run(serverWorld: ServerWorld, alien: AlienEntity, l: Long) {
        val world = alien.world

        if (targetPos == null) {
            val targetVec = locateShadedPos(alien) ?: return
            var targetPos = BlockPos(targetVec)
            val canMoveTo = alien.navigation.startMovingTo(targetVec.x, targetVec.y, targetVec.z, speed)

            if (!canMoveTo) {
                val offset = targetPos.add(0, 10, 0)

                for (i in 0 until 20) {
                    val newPos = offset.add(0, -i, 0)
                    val downPos = newPos.down()

                    if (world.getBlockState(newPos).isAir && world.getBlockState(downPos).isOpaque) {
                        targetPos = newPos

                        if (alien.navigation.startMovingTo(
                                targetPos.x.toDouble(),
                                targetPos.y.toDouble(),
                                targetPos.z.toDouble(),
                                speed
                            )
                        ) {
                            break
                        }
                    }
                }
            }

            this.targetPos = targetPos
        } else {
            val targetPos = this.targetPos ?: return
            if (targetPos.isWithinDistance(alien.pos, 4.0)) {
                alien.brain.remember(MemoryModuleType.HOME, GlobalPos.create(world.registryKey, targetPos))
                hasReachedNestingGround = true

                var resinPos: BlockPos
                for (x in -1..1) {
                    for (z in -1..1) {
                        resinPos = targetPos.add(x, 0, z)
                        val downPos = resinPos.down()

                        val travelUp = !(world.getBlockState(resinPos).isAir && world.getBlockState(downPos).isAir)

                        var i = 0
                        while (!world.getBlockState(resinPos).isAir || !world.getBlockState(downPos).isOpaque) {
                            resinPos.add(0, if(travelUp) 1 else -1, 0)
                            downPos.add(0, if(travelUp) 1 else -1, 0)

                            i++
                            if (i > 4) { // Limit search to prevent infinite loop
                                break
                            }
                        }

                        val topState = world.getBlockState(resinPos)
                        if (topState.isAir) {
                            val downState = world.getBlockState(downPos)
                            if (downState.block == Blocks.NEST_RESIN) {
                                world.setBlockState(downPos, downState.with(NestResinBlock.LAYERS, downState.get(NestResinBlock.LAYERS) + 1))
                            } else if (downState.isOpaqueFullCube(world, downPos)) {
                                world.setBlockState(resinPos, Blocks.NEST_RESIN.defaultState)
                            }
                        } else if (world.getBlockState(resinPos) == Blocks.NEST_RESIN.defaultState &&
                            topState.isOpaqueFullCube(world, resinPos)) {
                            world.setBlockState(resinPos, topState.with(NestResinBlock.LAYERS, topState.get(NestResinBlock.LAYERS) + 1))
                        }
                    }
                }
            }
        }
    }

    override fun finishRunning(world: ServerWorld, entity: AlienEntity, time: Long) {
        super.finishRunning(world, entity, time)
        hasReachedNestingGround = false
    }

    private fun locateShadedPos(entity: AlienEntity): Vec3d? {
        val random: Random = entity.random
        val blockPos: BlockPos = entity.blockPos

        if (!entity.world.isSkyVisible(blockPos) &&
            entity.world.getBlockState(blockPos).isAir &&
            entity.world.getBlockState(blockPos.down()).isOpaque &&
            entity.world.getLightLevel(LightType.SKY, blockPos) < 4) {
            return Vec3d.ofBottomCenter(blockPos)
        }

        for (i in 0..9) {
            val blockPos2 = blockPos.add(random.nextInt(32) - 16, random.nextInt(6) - 3, random.nextInt(32) - 16)
            if (!entity.world.isSkyVisible(blockPos2) &&
                entity.world.getBlockState(blockPos2).isAir &&
                entity.world.getBlockState(blockPos2.down()).isOpaque &&
                entity.world.getLightLevel(LightType.SKY, blockPos2) < 4) {
                return Vec3d.ofBottomCenter(blockPos2)
            }
        }
        return null
    }
}