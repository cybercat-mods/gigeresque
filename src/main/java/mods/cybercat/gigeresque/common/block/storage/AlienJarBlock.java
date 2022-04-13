package mods.cybercat.gigeresque.common.block.storage;

import java.util.stream.Stream;

import mods.cybercat.gigeresque.common.block.entity.JarStorageEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class AlienJarBlock extends AlienChestBlock {

	public AlienJarBlock() {
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return Stream.of(Block.createCuboidShape(5.5, 0, 5.5, 10.5, 3, 10.5),
				Block.createCuboidShape(5, 3, 5, 11, 9, 11), Block.createCuboidShape(4.5, 9, 4.5, 11.5, 16, 11.5),
				Block.createCuboidShape(4.5, 12, 4.5, 11.5, 16, 11.5), Block.createCuboidShape(5, 16, 5, 11, 18, 11))
				.reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof JarStorageEntity) {
				ItemScatterer.spawn(world, pos, (JarStorageEntity) blockEntity);
				world.updateComparators(pos, this);
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new JarStorageEntity(pos, state);
	}
}
