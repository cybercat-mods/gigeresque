//package com.bvanseg.gigeresque.common.block
//
//import net.minecraft.block.*
//import net.minecraft.item.ItemPlacementContext
//import net.minecraft.state.StateManager
//import net.minecraft.state.property.IntProperty
//import net.minecraft.state.property.Properties
//import net.minecraft.util.BlockMirror
//import net.minecraft.util.BlockRotation
//import net.minecraft.util.math.BlockPos
//import net.minecraft.util.math.MathHelper
//import net.minecraft.util.shape.VoxelShape
//import net.minecraft.util.shape.VoxelShapes
//import net.minecraft.world.BlockView
//
///**
// * @author Boston Vanseghi
// */
//class CustomSkullBlock(skullType: AlienSkullType, settings: Settings) : AbstractSkullBlock(skullType, settings) {
//
//    companion object {
//        val ROTATION: IntProperty = Properties.ROTATION
//        val SHAPE: VoxelShape = createCuboidShape(4.0, 0.0, 4.0, 12.0, 8.0, 12.0)
//    }
//
//    enum class AlienSkullType : SkullBlock.SkullType {
//        AQUA,
//        CLASSIC,
//        RUNNER,
//    }
//
//    init {
//        defaultState = (stateManager.defaultState as BlockState).with(ROTATION, 0) as BlockState
//    }
//
//    override fun getOutlineShape(
//        state: BlockState,
//        world: BlockView,
//        pos: BlockPos,
//        context: ShapeContext
//    ): VoxelShape {
//        return SHAPE
//    }
//
//    override fun getCullingShape(state: BlockState, world: BlockView, pos: BlockPos): VoxelShape {
//        return VoxelShapes.empty()
//    }
//
//    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
//        return defaultState.with(
//            ROTATION,
//            MathHelper.floor((ctx.playerYaw * 16.0f / 360.0f).toDouble() + 0.5) and 15
//        ) as BlockState
//    }
//
//    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
//        return state.with(ROTATION, rotation.rotate((state.get(ROTATION) as Int), 16)) as BlockState
//    }
//
//    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
//        return state.with(ROTATION, mirror.mirror((state.get(ROTATION) as Int), 16)) as BlockState
//    }
//
//    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
//        builder.add(ROTATION)
//    }
//}