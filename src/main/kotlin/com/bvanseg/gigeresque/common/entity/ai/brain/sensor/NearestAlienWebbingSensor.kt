//package com.bvanseg.gigeresque.common.entity.ai.brain.sensor
//
//import com.bvanseg.gigeresque.common.block.Blocks
//import com.bvanseg.gigeresque.common.entity.AlienEntity
//import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypes
//import com.bvanseg.gigeresque.common.util.nest.NestBuildingHelper
//import com.google.common.collect.ImmutableSet
//import net.minecraft.entity.ai.brain.MemoryModuleType
//import net.minecraft.entity.ai.brain.sensor.Sensor
//import net.minecraft.server.world.ServerWorld
//import net.minecraft.util.math.BlockPos
//import net.minecraft.util.math.Box
//import java.util.*
//
///**
// * @author Boston Vanseghi
// */
//class NearestAlienWebbingSensor : Sensor<AlienEntity>() {
//    companion object {
//        private fun findAlienWebbing(world: ServerWorld, alien: AlienEntity): Optional<BlockPos> {
//            return BlockPos.findClosest(
//                alien.blockPos, 8, 3
//            ) { pos: BlockPos ->
//                isAlienWebbing(
//                    alien,
//                    world,
//                    pos
//                )
//            }
//        }
//
//        private fun isAlienWebbing(alien: AlienEntity, world: ServerWorld, pos: BlockPos): Boolean {
//            val blockState = world.getBlockState(pos)
//            return blockState.block == Blocks.NEST_RESIN_WEB_CROSS &&
//                    (world.getBlockState(pos.up()).isAir || NestBuildingHelper.isResinBlock(world.getBlockState(pos.up()).block)) &&
//                    world.getBlockState(pos.down()).isOpaqueFullCube(world, pos) &&
//                    world.getOtherEntities(alien, Box(pos)).isEmpty()
//
//        }
//    }
//
//    override fun getOutputMemoryModules(): Set<MemoryModuleType<*>> {
//        return ImmutableSet.of(
//            MemoryModuleTypes.NEAREST_ALIEN_WEBBING
//        )
//    }
//
//    override fun sense(world: ServerWorld, alien: AlienEntity) {
//        val brain = alien.brain
//        brain.remember(MemoryModuleTypes.NEAREST_ALIEN_WEBBING, findAlienWebbing(world, alien))
//    }
//}