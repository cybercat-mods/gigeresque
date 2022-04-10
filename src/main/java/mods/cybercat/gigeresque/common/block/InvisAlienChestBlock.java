package mods.cybercat.gigeresque.common.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class InvisAlienChestBlock extends Block {

	public InvisAlienChestBlock() {
		super(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.GLOW_LICHEN).strength(5.0f, 8.0f)
				.nonOpaque());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult hit) {
		if (!world.isClient) {
			if (world.getBlockState(pos.down()).isOf(GIgBlocks.ALIEN_STORAGE_BLOCK_1)) {
				NamedScreenHandlerFactory screenHandlerFactory = world.getBlockState(pos.down())
						.createScreenHandlerFactory(world, pos.down());
				if (screenHandlerFactory != null) {
					player.openHandledScreen(screenHandlerFactory);
				}
			}
			if (world.getBlockState(pos.down().down()).isOf(GIgBlocks.ALIEN_STORAGE_BLOCK_1)) {
				NamedScreenHandlerFactory screenHandlerFactory = world.getBlockState(pos.down().down())
						.createScreenHandlerFactory(world, pos.down().down());
				if (screenHandlerFactory != null) {
					player.openHandledScreen(screenHandlerFactory);
				}
			}
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (world.getBlockState(pos.down()).isOf(GIgBlocks.ALIEN_STORAGE_BLOCK_1))
			world.breakBlock(pos.down(), true);
		if (world.getBlockState(pos.down()).isOf(this))
			world.setBlockState(pos.down(), Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
		if (world.getBlockState(pos.down().down()).isOf(GIgBlocks.ALIEN_STORAGE_BLOCK_1))
			world.breakBlock(pos.down().down(), true);
		if (world.getBlockState(pos.up()).isOf(GIgBlocks.ALIEN_STORAGE_BLOCK_1) || world.getBlockState(pos.up()).isOf(this))
			world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
		if (world.getBlockState(pos.up().up()).isOf(GIgBlocks.ALIEN_STORAGE_BLOCK_1) || world.getBlockState(pos.up().up()).isOf(this))
			world.setBlockState(pos.up().up(), Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return Block.createCuboidShape(0, 0, 0, 16, 16, 16);
	}

}
