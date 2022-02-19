//package com.bvanseg.gigeresque.common.block
//
//import com.bvanseg.gigeresque.common.entity.AlienEntity
//import com.bvanseg.gigeresque.common.util.clamp
//import net.minecraft.block.*
//import net.minecraft.block.Blocks
//import net.minecraft.entity.Entity
//import net.minecraft.entity.ai.pathing.NavigationType
//import net.minecraft.item.ItemPlacementContext
//import net.minecraft.state.StateManager
//import net.minecraft.state.property.IntProperty
//import net.minecraft.state.property.Properties
//import net.minecraft.util.math.BlockPos
//import net.minecraft.util.math.Direction
//import net.minecraft.util.math.Vec3d
//import net.minecraft.util.shape.VoxelShape
//import net.minecraft.util.shape.VoxelShapes
//import net.minecraft.world.BlockView
//import net.minecraft.world.World
//import net.minecraft.world.WorldAccess
//import net.minecraft.world.WorldView
//
///**
// * @author Boston Vanseghi
// */
//class NestResinBlock(settings: Settings) : Block(settings) {
//
//    companion object {
//        private fun interpolateShapes(divide: Boolean): List<VoxelShape> {
//            val list = mutableListOf<VoxelShape>()
//            for (i in 1..8) {
//                val minY = if (divide) (i * 2.0) / 2.0 else i * 2.0
//                list.add(createCuboidShape(0.0, 0.0, 0.0, 16.0, minY, 16.0))
//            }
//            return list
//        }
//
//        val LAYERS: IntProperty = Properties.LAYERS
//
//        val ALIEN_LAYERS_TO_SHAPE: Array<VoxelShape> = arrayOf(
//            VoxelShapes.empty(),
//            *interpolateShapes(false).toTypedArray()
//        )
//
//        val LAYERS_TO_SHAPE: Array<VoxelShape> = arrayOf(
//            VoxelShapes.empty(),
//            *interpolateShapes(true).toTypedArray()
//        )
//    }
//
//    init {
//        defaultState = (stateManager.defaultState as BlockState).with(LAYERS, 1) as BlockState
//    }
//
//    override fun canPathfindThrough(
//        state: BlockState,
//        world: BlockView,
//        pos: BlockPos,
//        type: NavigationType
//    ): Boolean = when (type) {
//        NavigationType.LAND -> true
//        else -> false
//    }
//
//    override fun getOutlineShape(
//        state: BlockState,
//        world: BlockView,
//        pos: BlockPos,
//        context: ShapeContext
//    ): VoxelShape = ALIEN_LAYERS_TO_SHAPE[(state.get(LAYERS) as Int)]
//
//    override fun getCollisionShape(
//        state: BlockState,
//        world: BlockView,
//        pos: BlockPos,
//        context: ShapeContext
//    ): VoxelShape = if (context is EntityShapeContext && context.entity is AlienEntity) {
//        ALIEN_LAYERS_TO_SHAPE[(state.get(LAYERS) as Int)]
//    } else {
//        LAYERS_TO_SHAPE[(state.get(LAYERS) as Int)]
//    }
//
//    override fun getSidesShape(
//        state: BlockState,
//        world: BlockView,
//        pos: BlockPos
//    ): VoxelShape = LAYERS_TO_SHAPE[(state.get(LAYERS) as Int)]
//
//    override fun getCameraCollisionShape(
//        state: BlockState,
//        world: BlockView,
//        pos: BlockPos,
//        context: ShapeContext
//    ): VoxelShape = LAYERS_TO_SHAPE[(state.get(LAYERS) as Int)]
//
//    override fun hasSidedTransparency(state: BlockState): Boolean = true
//
//    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
//        val blockState = world.getBlockState(pos.down())
//        return if (!blockState.isOf(Blocks.ICE) && !blockState.isOf(Blocks.PACKED_ICE) && !blockState.isOf(Blocks.BARRIER)) {
//            if (!blockState.isOf(Blocks.HONEY_BLOCK) && !blockState.isOf(Blocks.SOUL_SAND)) {
//                isFaceFullSquare(
//                    blockState.getCollisionShape(world, pos.down()),
//                    Direction.UP
//                ) || blockState.isOf(this) && blockState.get(
//                    LAYERS
//                ) as Int == 8
//            } else {
//                true
//            }
//        } else {
//            false
//        }
//    }
//
//    override fun getStateForNeighborUpdate(
//        state: BlockState,
//        direction: Direction,
//        neighborState: BlockState,
//        world: WorldAccess,
//        pos: BlockPos,
//        neighborPos: BlockPos
//    ): BlockState = if (!state.canPlaceAt(world, pos)) {
//        Blocks.AIR.defaultState
//    } else {
//        super.getStateForNeighborUpdate(
//            state,
//            direction,
//            neighborState,
//            world,
//            pos,
//            neighborPos
//        )
//    }
//
//    override fun canReplace(state: BlockState, context: ItemPlacementContext): Boolean {
//        val i = state.get(LAYERS) as Int
//        return if (context.stack.isOf(asItem()) && i < 8) {
//            if (context.canReplaceExisting()) {
//                context.side == Direction.UP
//            } else {
//                true
//            }
//        } else {
//            i == 1
//        }
//    }
//
//    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
//        val blockState = ctx.world.getBlockState(ctx.blockPos)
//        return if (blockState.isOf(this)) {
//            val i = blockState.get(LAYERS) as Int
//            blockState.with(LAYERS, 8.coerceAtMost(i + 1)) as BlockState
//        } else {
//            super.getPlacementState(ctx)
//        }
//    }
//
//    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
//        builder.add(LAYERS)
//    }
//
//    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
//        if (entity !is AlienEntity) {
//            val multiplier = clamp(1.0 / (state.get(LAYERS)), 0.0, 1.0)
//            entity.slowMovement(state, Vec3d(1.0 * multiplier, 1.0, 1.0 * multiplier))
//        }
//    }
//}