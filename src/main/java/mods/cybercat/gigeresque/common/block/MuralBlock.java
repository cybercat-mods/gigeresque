package mods.cybercat.gigeresque.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MuralBlock extends GigBlock {
	private BlockPos lightBlockPos = null;

	public MuralBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		spawnBlock(player, true, pos);
		super.onBreak(world, pos, state, player);
	}

	private void spawnBlock(LivingEntity entity, boolean isInWaterBlock, BlockPos pos) {
		if (lightBlockPos == null) {
			lightBlockPos = findFreeSpace(entity.world, pos, 1);
			if (lightBlockPos == null)
				return;
			entity.world.setBlockState(lightBlockPos, GIgBlocks.ORGANIC_ALIEN_BLOCK.getDefaultState());
		} else
			lightBlockPos = null;
	}

	private BlockPos findFreeSpace(World world, BlockPos blockPos, int maxDistance) {
		if (blockPos == null)
			return null;

		int[] offsets = new int[maxDistance * 2 + 1];
		offsets[0] = 0;
		for (int i = 2; i <= maxDistance * 2; i += 2) {
			offsets[i - 1] = i / 2;
			offsets[i] = -i / 2;
		}
		for (int x : offsets)
			for (int y : offsets)
				for (int z : offsets) {
					BlockPos offsetPos = blockPos.add(x, y, z);
					BlockState state = world.getBlockState(offsetPos);
					if (!state.isAir())
						return offsetPos;
				}

		return null;
	}

}
