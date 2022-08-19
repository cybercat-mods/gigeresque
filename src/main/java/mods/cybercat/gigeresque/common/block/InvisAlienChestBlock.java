package mods.cybercat.gigeresque.common.block;

import mods.cybercat.gigeresque.common.block.entity.AlienStorageEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class InvisAlienChestBlock extends Block {

	BlockPos[] blockPoss;

	public InvisAlienChestBlock() {
		super(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.GLOW_LICHEN).strength(5.0f, 8.0f)
				.nonOpaque());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult hit) {
		if (!world.isClient) {
			for (BlockPos blockPos : blockPoss = new BlockPos[] { pos.down(), pos.down().down() }) {
				if (world.getBlockState(blockPos).isOf(GIgBlocks.ALIEN_STORAGE_BLOCK_1)) {
					BlockEntity blockEntity = world.getBlockEntity(blockPos);
					if (blockEntity instanceof AlienStorageEntity) {
						player.openHandledScreen((AlienStorageEntity) blockEntity);
					}
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
		for (BlockPos blockPos : blockPoss = new BlockPos[] { pos.down(), pos.down().down(), pos.up(),
				pos.up().up() }) {
			if (world.getBlockState(blockPos).isOf(GIgBlocks.ALIEN_STORAGE_BLOCK_1)) {
				world.breakBlock(blockPos, true);
			}
			if (world.getBlockState(blockPos).isOf(this)) {
				world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
			}
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return Block.createCuboidShape(0, 0, 0, 16, 16, 16);
	}

}
