package mods.cybercat.gigeresque.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class CustomStairsBlock extends StairBlock {
	public CustomStairsBlock(BlockState baseBlockState, Properties settings) {
		super(baseBlockState, settings);
	}

	public CustomStairsBlock(Block block, Properties settings) {
		this(block.defaultBlockState(), settings);
	}

	public CustomStairsBlock(Block block) {
		this(block, Properties.copy(block));
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
		return false;
	}
}
