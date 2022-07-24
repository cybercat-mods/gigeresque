package mods.cybercat.gigeresque.common.block.storage;

import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class AlienSarcophagusBlock extends AlienChestBlock {

	public AlienSarcophagusBlock() {
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
		world.setBlockState(pos.up().up(), Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
		super.onBreak(world, pos, state, player);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (world.getBlockState(pos.up()).isAir() && world.getBlockState(pos.up().up()).isAir()) {
			world.setBlockState(pos.up(), GIgBlocks.ALIEN_STORAGE_BLOCK_INVIS.getDefaultState(), Block.NOTIFY_ALL);
			world.setBlockState(pos.up().up(), GIgBlocks.ALIEN_STORAGE_BLOCK_INVIS.getDefaultState(), Block.NOTIFY_ALL);
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return Block.createCuboidShape(0, 0, 0, 16, 16, 16);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.up()).isAir() && world.getBlockState(pos.up().up()).isAir();
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof AlienStorageEntity) {
			((AlienStorageEntity) blockEntity).onScheduledTick();
		}
	}
}
