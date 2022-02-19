//package com.bvanseg.gigeresque.common.block
//
//import net.minecraft.block.Block
//import net.minecraft.block.BlockState
//import net.minecraft.block.LandingBlock
//import net.minecraft.entity.FallingBlockEntity
//import net.minecraft.server.world.ServerWorld
//import net.minecraft.tag.BlockTags
//import net.minecraft.util.math.BlockPos
//import net.minecraft.util.math.Direction
//import net.minecraft.world.World
//import net.minecraft.world.WorldAccess
//import java.util.*
//
///**
// * @author Boston Vanseghi
// */
//open class CustomFallingBlock(settings: Settings?) : Block(settings), LandingBlock {
//
//    open fun canFallThrough(state: BlockState): Boolean {
//        val material = state.material
//        return state.isAir || state.isIn(BlockTags.FIRE) || material.isLiquid || material.isReplaceable
//    }
//
//    override fun onBlockAdded(
//        state: BlockState,
//        world: World,
//        pos: BlockPos,
//        oldState: BlockState,
//        notify: Boolean
//    ) = world.createAndScheduleBlockTick(pos, this, getFallDelay())
//
//    override fun getStateForNeighborUpdate(
//        state: BlockState,
//        direction: Direction,
//        neighborState: BlockState,
//        world: WorldAccess,
//        pos: BlockPos?,
//        neighborPos: BlockPos?
//    ): BlockState {
//        world.createAndScheduleBlockTick(pos, this, getFallDelay())
//        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
//    }
//
//    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
//        if (canFallThrough(world.getBlockState(pos.down())) && pos.y >= world.bottomY) {
//            val fallingBlockEntity = FallingBlockEntity(
//                world, pos.x.toDouble() + 0.5,
//                pos.y.toDouble(), pos.z.toDouble() + 0.5, world.getBlockState(pos)
//            )
//            world.spawnEntity(fallingBlockEntity)
//        }
//    }
//
//    private fun getFallDelay(): Int = 2
//}