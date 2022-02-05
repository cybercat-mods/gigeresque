package com.bvanseg.gigeresque.common.entity.ai.brain.sensor

import com.bvanseg.gigeresque.common.block.tag.BlockTags
import com.google.common.collect.ImmutableSet
import java.util.Optional
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.sensor.Sensor
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

/**
 * @author Boston Vanseghi
 */
class AlienRepellentSensor : Sensor<LivingEntity>() {

    companion object {
        private fun findAlienRepellent(world: ServerWorld, entity: LivingEntity): Optional<BlockPos> {
            return BlockPos.findClosest(
                entity.blockPos, 8, 4
            ) { pos: BlockPos ->
                isAlienRepellent(
                    world,
                    pos
                )
            }
        }

        private fun isAlienRepellent(world: ServerWorld, pos: BlockPos): Boolean {
            val blockState = world.getBlockState(pos)
            return blockState.isIn(BlockTags.ALIEN_REPELLENTS)
        }
    }

    override fun getOutputMemoryModules(): Set<MemoryModuleType<*>> {
        return ImmutableSet.of(
            MemoryModuleType.NEAREST_REPELLENT
        )
    }

    override fun sense(world: ServerWorld, entity: LivingEntity) {
        val brain = entity.brain
        brain.remember(MemoryModuleType.NEAREST_REPELLENT, findAlienRepellent(world, entity))
    }
}