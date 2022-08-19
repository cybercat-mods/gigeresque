package mods.cybercat.gigeresque.common.block;

import mods.cybercat.gigeresque.common.block.entity.IdolStorageEntity;
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

public class InvisAlienChest2Block extends Block {

	BlockPos[] blockPoss;

	public InvisAlienChest2Block() {
		super(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.GLOW_LICHEN).strength(5.0f, 8.0f)
				.nonOpaque());
	}

	protected boolean isTarget(World world, BlockPos pos) {
		return world.getBlockState(pos).isOf(GIgBlocks.ALIEN_STORAGE_BLOCK_3);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult hit) {
		if (!world.isClient) {
			for (BlockPos blockPos : blockPoss = new BlockPos[] { pos.west(), pos.east(), pos.north(), pos.south(),
					pos.west().west(), pos.east().east(), pos.north().north(), pos.south().south(), pos.down(),
					pos.down().down(), pos.east().down(), pos.east().east().down(), pos.east().down().down(),
					pos.east().east().down().down(), pos.down().west(), pos.down().down().west(),
					pos.down().west().west(), pos.down().down().west().west(), pos.down().south(),
					pos.down().down().south(), pos.down().south().south(), pos.down().down().south().south(),
					pos.down().north(), pos.down().down().north(), pos.down().north().north(),
					pos.down().down().north().north() }) {
				if (world.getBlockState(blockPos).isOf(GIgBlocks.ALIEN_STORAGE_BLOCK_3)) {
					BlockEntity blockEntity = world.getBlockEntity(blockPos);
					if (blockEntity instanceof IdolStorageEntity) {
						player.openHandledScreen((IdolStorageEntity) blockEntity);
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
		for (BlockPos blockPos : blockPoss = new BlockPos[] { pos.west(), pos.east(), pos.north(), pos.south(),
				pos.west().west(), pos.east().east(), pos.north().north(), pos.south().south(), pos.down(),
				pos.down().down(), pos.east().down(), pos.east().east().down(), pos.east().down().down(),
				pos.east().east().down().down(), pos.down().west(), pos.down().down().west(), pos.down().west().west(),
				pos.down().down().west().west(), pos.down().south(), pos.down().down().south(),
				pos.down().south().south(), pos.down().down().south().south(), pos.down().north(),
				pos.down().down().north(), pos.down().north().north(), pos.down().down().north().north(), pos.up(),
				pos.up().up(), pos.up().west(), pos.up().up().west(), pos.up().west().west(),
				pos.up().up().west().west(), pos.up().south(), pos.up().up().south(), pos.up().south().south(),
				pos.up().up().south().south(), pos.up().north(), pos.up().up().north(), pos.up().north().north(),
				pos.up().up().north().north(), pos.up().east(), pos.up().east().east(), pos.east().up(),
				pos.east().up().up(), pos.east().east().up(), pos.east().east().up().up(), pos.west().west().up(),
				pos.west().west().up().up(), pos.north().north().up(), pos.north().north().up().up(),
				pos.south().south().up(), pos.south().south().up().up() }) {
			if (world.getBlockState(blockPos).isOf(GIgBlocks.ALIEN_STORAGE_BLOCK_3)) {
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