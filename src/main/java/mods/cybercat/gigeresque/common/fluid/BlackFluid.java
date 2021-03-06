package mods.cybercat.gigeresque.common.fluid;

import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.item.GigItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public abstract class BlackFluid extends FlowableFluid {
	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == GigFluids.BLACK_FLUID_STILL || fluid == GigFluids.BLACK_FLUID_FLOWING;
	}

	@Override
	public Item getBucketItem() {
		return GigItems.BLACK_FLUID_BUCKET;
	}

	@Override
	public boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid,
			Direction direction) {
		return false;
	}

	@Override
	public int getTickRate(WorldView world) {
		return 20;
	}

	@Override
	protected float getBlastResistance() {
		return 100.0f;
	}

	@Override
	protected BlockState toBlockState(FluidState state) {
		return GIgBlocks.BLACK_FLUID.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(state));
	}

	@Override
	public Fluid getFlowing() {
		return GigFluids.BLACK_FLUID_FLOWING;
	}

	@Override
	public Fluid getStill() {
		return GigFluids.BLACK_FLUID_STILL;
	}

	@Override
	protected boolean isInfinite() {
		return false;
	}

	@Override
	protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
		BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
		Block.dropStacks(state, world, pos, blockEntity);
	}

	@Override
	protected int getFlowSpeed(WorldView world) {
		return 2;
	}

	@Override
	protected int getLevelDecreasePerBlock(WorldView world) {
		return 2;
	}

	static class Flowing extends BlackFluid {
		@Override
		public void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
			super.appendProperties(builder);
			builder.add(LEVEL);
		}

		@Override
		public int getLevel(FluidState fluidState) {
			return fluidState.get(LEVEL);
		}

		@Override
		public boolean isStill(FluidState fluidState) {
			return false;
		}
	}

	static class Still extends BlackFluid {

		@Override
		public int getLevel(FluidState fluidState) {
			return 8;
		}

		@Override
		public boolean isStill(FluidState fluidState) {
			return true;
		}
	}
}
