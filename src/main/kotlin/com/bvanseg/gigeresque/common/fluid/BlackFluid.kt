package com.bvanseg.gigeresque.common.fluid

import com.bvanseg.gigeresque.common.block.Blocks
import com.bvanseg.gigeresque.common.item.Items
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.fluid.FlowableFluid
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView


/**
 * @author Boston Vanseghi
 */
abstract class BlackFluid : FlowableFluid() {

    override fun matchesType(fluid: Fluid?): Boolean {
        return fluid == Fluids.BLACK_FLUID_STILL || fluid == Fluids.BLACK_FLUID_FLOWING
    }

    override fun getBucketItem(): Item = Items.BLACK_FLUID_BUCKET

    override fun canBeReplacedWith(
        state: FluidState?,
        world: BlockView?,
        pos: BlockPos?,
        fluid: Fluid?,
        direction: Direction?
    ): Boolean = false

    override fun getTickRate(world: WorldView?): Int = 20

    override fun getBlastResistance(): Float = 100.0f

    override fun toBlockState(state: FluidState?): BlockState = Blocks.BLACK_FLUID.defaultState.with(
        Properties.LEVEL_15, getBlockStateLevel(state)
    )

    override fun getFlowing(): Fluid = Fluids.BLACK_FLUID_FLOWING
    override fun getStill(): Fluid = Fluids.BLACK_FLUID_STILL

    override fun isInfinite(): Boolean = false

    override fun beforeBreakingBlock(world: WorldAccess, pos: BlockPos?, state: BlockState) {
        val blockEntity = if (state.hasBlockEntity()) world.getBlockEntity(pos) else null
        Block.dropStacks(state, world, pos, blockEntity)
    }

    override fun getFlowSpeed(world: WorldView?): Int = 2
    override fun getLevelDecreasePerBlock(world: WorldView?): Int = 2

    class Flowing : BlackFluid() {
        override fun appendProperties(builder: StateManager.Builder<Fluid, FluidState>) {
            super.appendProperties(builder)
            builder.add(LEVEL)
        }

        override fun getLevel(fluidState: FluidState): Int = fluidState.get(LEVEL)
        override fun isStill(fluidState: FluidState): Boolean = false
    }

    class Still : BlackFluid() {
        override fun getLevel(fluidState: FluidState): Int = 8
        override fun isStill(fluidState: FluidState): Boolean = true
    }
}