package mods.cybercat.gigeresque.common.block;

import mods.cybercat.gigeresque.common.block.entity.IdolStorageEntity;
import mods.cybercat.gigeresque.common.block.material.Materials;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class InvisAlienChest2Block extends Block {

	private static final VoxelShape OUTLINE_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 16, 16);

	public InvisAlienChest2Block() {
		super(FabricBlockSettings.of(Materials.ORGANIC_ALIEN_BLOCK).sounds(BlockSoundGroup.GLOW_LICHEN).strength(5.0f, 8.0f)
				.nonOpaque().dropsNothing());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult hit) {
		if (!world.isClient) {
			Vec3i radius = new Vec3i(2, 2, 2);

			for (BlockPos testPos : BlockPos.iterate(pos.subtract(radius), pos.add(radius))) {
				if (world.getBlockState(testPos).isOf(GIgBlocks.ALIEN_STORAGE_BLOCK_3)) {
					if (!world.isClient
							&& world.getBlockEntity(testPos)instanceof IdolStorageEntity idolStorageEntity) {
						player.openHandledScreen(idolStorageEntity);
					}

					return ActionResult.SUCCESS;
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
		if (world.isClient)
			return;

		Vec3i radius = new Vec3i(2, 2, 2);

		for (BlockPos testPos : BlockPos.iterate(pos.subtract(radius), pos.add(radius))) {
			BlockState testState;

			if ((testState = world.getBlockState(testPos)).isOf(GIgBlocks.ALIEN_STORAGE_BLOCK_3)) {
				world.breakBlock(testPos, true);
			} else if (testState.isOf(this)) {
				world.setBlockState(testPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
			}
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return OUTLINE_SHAPE;
	}
}