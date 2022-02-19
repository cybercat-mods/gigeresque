//package com.bvanseg.gigeresque.common.entity.ai.brain.sensor
//
//import com.bvanseg.gigeresque.common.block.tag.BlockTags
//import com.bvanseg.gigeresque.common.entity.AlienEntity
//import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypes
//import com.google.common.collect.ImmutableSet
//import net.minecraft.entity.ai.brain.MemoryModuleType
//import net.minecraft.entity.ai.brain.sensor.Sensor
//import net.minecraft.server.world.ServerWorld
//import net.minecraft.util.math.BlockPos
//import java.util.*
//
///**
// * @author Boston Vanseghi
// */
//class DestructibleLightSensor : Sensor<AlienEntity>() {
//    companion object {
//        private fun findDestructibleLight(world: ServerWorld, alien: AlienEntity): Optional<BlockPos> {
//            return BlockPos.findClosest(
//                alien.blockPos, 8, 3
//            ) { pos: BlockPos ->
//                isDestructibleLight(
//                    world,
//                    pos
//                )
//            }
//        }
//
//        private fun isDestructibleLight(world: ServerWorld, pos: BlockPos): Boolean {
//            val blockState = world.getBlockState(pos)
//            return blockState.isIn(BlockTags.DESTRUCTIBLE_LIGHT)
//        }
//    }
//
//    override fun getOutputMemoryModules(): Set<MemoryModuleType<*>> {
//        return ImmutableSet.of(
//            MemoryModuleTypes.NEAREST_LIGHT_SOURCE
//        )
//    }
//
//    override fun sense(world: ServerWorld, alien: AlienEntity) {
//        val brain = alien.brain
//        brain.remember(MemoryModuleTypes.NEAREST_LIGHT_SOURCE, findDestructibleLight(world, alien))
//    }
//}