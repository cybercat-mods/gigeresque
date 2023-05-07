package mods.cybercat.gigeresque.common.block.storage;

import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.block.entity.IdolStorageEntity;
import mods.cybercat.gigeresque.common.block.material.Materials;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SittingIdolInvisBlock extends Block {

	private static final VoxelShape OUTLINE_SHAPE = Block.box(0, 0, 0, 16, 16, 16);

	public SittingIdolInvisBlock() {
		super(FabricBlockSettings.of(Materials.ORGANIC_ALIEN_BLOCK).sounds(SoundType.DRIPSTONE_BLOCK).strength(5.0f, 8.0f).nonOpaque().noLootTable());
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!world.isClientSide) {
			var radius = new Vec3i(2, 2, 2);
			for (BlockPos testPos : BlockPos.betweenClosed(pos.subtract(radius), pos.offset(radius)))
				if (world.getBlockState(testPos).is(GIgBlocks.ALIEN_STORAGE_BLOCK_3)) {
					if (!world.isClientSide && world.getBlockEntity(testPos)instanceof IdolStorageEntity idolStorageEntity)
						player.openMenu(idolStorageEntity);
					return InteractionResult.SUCCESS;
				}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (world.isClientSide)
			return;

		var radius = new Vec3i(2, 2, 2);

		for (BlockPos testPos : BlockPos.betweenClosed(pos.subtract(radius), pos.offset(radius))) {
			BlockState testState;

			if ((testState = world.getBlockState(testPos)).is(GIgBlocks.ALIEN_STORAGE_BLOCK_3))
				world.destroyBlock(testPos, true);
			else if (testState.is(this))
				world.setBlock(testPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return OUTLINE_SHAPE;
	}
}