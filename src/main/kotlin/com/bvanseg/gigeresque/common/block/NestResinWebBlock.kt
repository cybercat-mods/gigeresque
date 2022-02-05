package com.bvanseg.gigeresque.common.block

import net.minecraft.block.*
import net.minecraft.block.Blocks
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Util
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import java.util.function.Function
import java.util.stream.Collectors

/**
 * @author Boston Vanseghi
 */
class NestResinWebBlock(settings: Settings) : Block(settings) {

    companion object {
        val UP: BooleanProperty = ConnectingBlock.UP
        val NORTH: BooleanProperty = ConnectingBlock.NORTH
        val EAST: BooleanProperty = ConnectingBlock.EAST
        val SOUTH: BooleanProperty = ConnectingBlock.SOUTH
        val WEST: BooleanProperty = ConnectingBlock.WEST
        val FACING_PROPERTIES: Map<Direction, BooleanProperty> = ConnectingBlock.FACING_PROPERTIES.entries.stream()
            .filter { (key, _) -> key != Direction.DOWN }.collect(Util.toMap())
        val VARIANTS: EnumProperty<NestResinWebVariant> =
            EnumProperty.of("nest_resin_web_variant", NestResinWebVariant::class.java)

        private val UP_SHAPE = createCuboidShape(0.0, 15.0, 0.0, 16.0, 16.0, 16.0)
        private val EAST_SHAPE = createCuboidShape(0.0, 0.0, 0.0, 1.0, 16.0, 16.0)
        private val WEST_SHAPE = createCuboidShape(15.0, 0.0, 0.0, 16.0, 16.0, 16.0)
        private val SOUTH_SHAPE = createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 1.0)
        private val NORTH_SHAPE = createCuboidShape(0.0, 0.0, 15.0, 16.0, 16.0, 16.0)
        private fun getShapeForState(state: BlockState): VoxelShape {
            var voxelShape = VoxelShapes.empty()
            if (state.get(UP) as Boolean) {
                voxelShape = UP_SHAPE
            }
            if (state.get(NORTH) as Boolean) {
                voxelShape = VoxelShapes.union(voxelShape, SOUTH_SHAPE)
            }
            if (state.get(SOUTH) as Boolean) {
                voxelShape = VoxelShapes.union(voxelShape, NORTH_SHAPE)
            }
            if (state.get(EAST) as Boolean) {
                voxelShape = VoxelShapes.union(voxelShape, WEST_SHAPE)
            }
            if (state.get(WEST) as Boolean) {
                voxelShape = VoxelShapes.union(voxelShape, EAST_SHAPE)
            }
            return if (voxelShape.isEmpty) VoxelShapes.fullCube() else voxelShape
        }

        fun shouldConnectTo(world: BlockView, pos: BlockPos, direction: Direction): Boolean {
            val blockState = world.getBlockState(pos)
            return isFaceFullSquare(blockState.getCollisionShape(world, pos), direction.opposite)
        }

        fun getFacingProperty(direction: Direction): BooleanProperty? {
            return FACING_PROPERTIES[direction]
        }
    }

    private val shapesByState: Map<BlockState, VoxelShape>

    init {
        defaultState = stateManager.defaultState
            .with(UP, false)
            .with(NORTH, false)
            .with(EAST, false)
            .with(SOUTH, false)
            .with(WEST, false)
            .with(VARIANTS, NestResinWebVariant.ONE)

        shapesByState = stateManager.states.stream().collect(
            Collectors.toMap(Function.identity()) {
                getShapeForState(it)
            }
        )
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape? {
        return shapesByState[state]
    }

    override fun isTranslucent(state: BlockState, world: BlockView, pos: BlockPos): Boolean {
        return true
    }

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        return hasAdjacentBlocks(getPlacementShape(state, world, pos))
    }

    private fun hasAdjacentBlocks(state: BlockState): Boolean {
        return getAdjacentBlockCount(state) > 0
    }

    private fun getAdjacentBlockCount(state: BlockState): Int {
        var i = 0
        val var3: Iterator<*> = FACING_PROPERTIES.values.iterator()
        while (var3.hasNext()) {
            val booleanProperty = var3.next() as BooleanProperty
            if (state.get(booleanProperty) as Boolean) {
                ++i
            }
        }
        return i
    }

    private fun shouldHaveSide(world: BlockView, pos: BlockPos, side: Direction): Boolean {
        return if (side == Direction.DOWN) {
            false
        } else {
            val blockPos = pos.offset(side)
            if (shouldConnectTo(world, blockPos, side)) {
                true
            } else if (side.axis === Direction.Axis.Y) {
                false
            } else {
                val blockState = world.getBlockState(pos.up())
                blockState.isOf(this) && blockState.get(FACING_PROPERTIES[side]) as Boolean
            }
        }
    }

    private fun getPlacementShape(state: BlockState, world: BlockView, pos: BlockPos): BlockState {
        var state = state
        val blockPos = pos.up()
        if (state.get(UP) as Boolean) {
            state = state.with(UP, shouldConnectTo(world, blockPos, Direction.DOWN)) as BlockState
        }
        var blockState: BlockState? = null
        val var6: Iterator<*> = Direction.Type.HORIZONTAL.iterator()
        while (true) {
            var direction: Direction
            var booleanProperty: BooleanProperty?
            do {
                if (!var6.hasNext()) {
                    return state
                }
                direction = var6.next() as Direction
                booleanProperty = getFacingProperty(direction)
            } while (!state.get(booleanProperty))
            var bl = shouldHaveSide(world, pos, direction)
            if (!bl) {
                if (blockState == null) {
                    blockState = world.getBlockState(blockPos)
                }
                bl = blockState!!.isOf(this) && blockState.get(booleanProperty) as Boolean
            }
            state = state.with(booleanProperty, bl) as BlockState
        }
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        return if (direction == Direction.DOWN) {
            super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
        } else {
            val blockState = getPlacementShape(state, world, pos)
            if (!hasAdjacentBlocks(blockState)) Blocks.AIR.defaultState else blockState
        }
    }

    override fun canReplace(state: BlockState, context: ItemPlacementContext): Boolean {
        val blockState = context.world.getBlockState(context.blockPos)
        return if (blockState.isOf(this)) {
            getAdjacentBlockCount(blockState) < FACING_PROPERTIES.size
        } else {
            super.canReplace(state, context)
        }
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val blockState = ctx.world.getBlockState(ctx.blockPos)
        val bl = blockState.isOf(this)
        val blockState2 = if (bl) blockState else defaultState
        val var5 = ctx.placementDirections
        val var6 = var5.size
        for (var7 in 0 until var6) {
            val direction = var5[var7]
            if (direction != Direction.DOWN) {
                val booleanProperty = getFacingProperty(direction)
                val bl2 = bl && blockState.get(booleanProperty) as Boolean
                if (!bl2 && shouldHaveSide(ctx.world, ctx.blockPos, direction)) {
                    return blockState2.with(booleanProperty, true).with(VARIANTS, NestResinWebVariant.values().random())
                }
            }
        }
        return if (bl) blockState2 else null
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(UP, NORTH, EAST, SOUTH, WEST, VARIANTS)
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return when (rotation) {
            BlockRotation.CLOCKWISE_180 -> (((state.with(NORTH, state.get(SOUTH) as Boolean) as BlockState).with(
                EAST, state.get(WEST) as Boolean
            ) as BlockState).with(SOUTH, state.get(NORTH) as Boolean) as BlockState).with(
                WEST, state.get(EAST) as Boolean
            ) as BlockState
            BlockRotation.COUNTERCLOCKWISE_90 -> (((state.with(NORTH, state.get(EAST) as Boolean) as BlockState).with(
                EAST, state.get(SOUTH) as Boolean
            ) as BlockState).with(SOUTH, state.get(WEST) as Boolean) as BlockState).with(
                WEST, state.get(NORTH) as Boolean
            ) as BlockState
            BlockRotation.CLOCKWISE_90 -> (((state.with(NORTH, state.get(WEST) as Boolean) as BlockState).with(
                EAST, state.get(
                    NORTH
                ) as Boolean
            ) as BlockState).with(SOUTH, state.get(EAST) as Boolean) as BlockState).with(
                WEST, state.get(
                    SOUTH
                ) as Boolean
            ) as BlockState
            else -> state
        }
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return when (mirror) {
            BlockMirror.LEFT_RIGHT -> (state.with(NORTH, state.get(SOUTH) as Boolean) as BlockState).with(
                SOUTH, state.get(NORTH) as Boolean
            ) as BlockState
            BlockMirror.FRONT_BACK -> (state.with(EAST, state.get(WEST) as Boolean) as BlockState).with(
                WEST, state.get(
                    EAST
                ) as Boolean
            ) as BlockState
            else -> super.mirror(state, mirror)
        }
    }
}