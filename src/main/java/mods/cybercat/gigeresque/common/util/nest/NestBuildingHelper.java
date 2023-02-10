package mods.cybercat.gigeresque.common.util.nest;

import java.util.List;
import java.util.Random;
import java.util.SplittableRandom;
import java.util.concurrent.atomic.AtomicInteger;

import com.mojang.datafixers.util.Pair;

import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.block.NestResinWebBlock;
import mods.cybercat.gigeresque.common.block.NestResinWebVariant;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class NestBuildingHelper {
	private NestBuildingHelper() {
	}

	public static void tryBuildNestAround(AlienEntity alien) {
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				for (int y = -1; y <= 3; y++) {
					var blockPos = alien.blockPosition().offset(x, y, z);
					var nestBlockData = getNestBlockData(alien.level, blockPos);
					if (nestBlockData == null)
						continue;

					if (alien.getLevel().getLightEmission(alien.blockPosition()) < 8) {
						var random = new SplittableRandom();
						var randomPhase = random.nextInt(0, 100);
						var resinBlock = GIgBlocks.NEST_RESIN.defaultBlockState();
						if (nestBlockData.isFloor())
							if (randomPhase <= 70)
								alien.level.setBlockAndUpdate(blockPos, resinBlock);
							else
								alien.level.setBlockAndUpdate(blockPos, resinBlock);

						if (nestBlockData.isCorner())
							alien.level.setBlockAndUpdate(blockPos, GIgBlocks.NEST_RESIN_WEB_CROSS.defaultBlockState());

						if (nestBlockData.isWall() || nestBlockData.isCeiling()) {
							var nestResinWebState = GIgBlocks.NEST_RESIN_WEB.defaultBlockState()
									.setValue(NestResinWebBlock.UP, nestBlockData.hasUpCoverage())
									.setValue(NestResinWebBlock.NORTH, nestBlockData.hasNorthCoverage())
									.setValue(NestResinWebBlock.SOUTH, nestBlockData.hasSouthCoverage())
									.setValue(NestResinWebBlock.EAST, nestBlockData.hasEastCoverage())
									.setValue(NestResinWebBlock.WEST, nestBlockData.hasWestCoverage())
									.setValue(NestResinWebBlock.VARIANTS, NestResinWebVariant.values()[new Random()
											.nextInt(NestResinWebVariant.values().length)]);
							alien.level.setBlockAndUpdate(blockPos, nestResinWebState);
						}
						if (alien.level.isClientSide)
							alien.playSound(SoundEvents.HONEY_BLOCK_STEP);
					}
				}
			}
		}
	}

	public static boolean isResinBlock(Block block) {
		return block == GIgBlocks.NEST_RESIN || block == GIgBlocks.NEST_RESIN_WEB
				|| block == GIgBlocks.NEST_RESIN_WEB_CROSS || block == GIgBlocks.NEST_RESIN_BLOCK;
	}

	private static NestBlockData getNestBlockData(Level world, BlockPos blockPos) {
		var actualState = world.getBlockState(blockPos);
		var actualBlock = actualState.getBlock();

		if (isResinBlock(actualBlock))
			return null;

		var upPos = blockPos.above();
		var upState = world.getBlockState(upPos);

		var downPos = blockPos.below();
		var downState = world.getBlockState(downPos);

		var northPos = blockPos.north();
		var northState = world.getBlockState(northPos);

		var southPos = blockPos.south();
		var southState = world.getBlockState(southPos);

		var eastPos = blockPos.east();
		var eastState = world.getBlockState(eastPos);

		var westPos = blockPos.west();
		var westState = world.getBlockState(westPos);

		var blockStates = List.of(Pair.of(upPos, upState), Pair.of(downPos, downState), Pair.of(northPos, northState),
				Pair.of(southPos, southState), Pair.of(eastPos, eastState), Pair.of(westPos, westState));

		var fullCoverage = new AtomicInteger();
		var horizontalCoverage = new AtomicInteger();
		var cornerCoverage = new AtomicInteger();

		blockStates.forEach(it -> {
			var isOpaqueCube = it.getSecond().isSolidRender(world, it.getFirst());

			if (isOpaqueCube)
				if (!isResinBlock(it.getSecond().getBlock())) {
					if (it.getFirst() != upPos && it.getFirst() != downPos)
						horizontalCoverage.getAndIncrement();
					fullCoverage.getAndIncrement();
				} else
					cornerCoverage.getAndIncrement();
		});

		var isFloor = actualState.isAir() && upState.isAir() && !isResinBlock(downState.getBlock())
				&& downState.isSolidRender(world, downPos) && world.getLightEmission(blockPos) < 10;
		var isWall = !isFloor && actualState.isAir() && (upState.isAir() || isResinBlock(upState.getBlock()))
				&& (1 <= horizontalCoverage.get() && horizontalCoverage.get() <= 2)
				&& world.getLightEmission(blockPos) < 10;
		var isCorner = actualState.isAir() && (3 <= fullCoverage.get() && fullCoverage.get() <= 5)
				&& world.getLightEmission(blockPos) < 10;
		var isCeiling = actualState.isAir() && (downState.isAir() || isResinBlock(downState.getBlock()))
				&& upState.isSolidRender(world, downPos) && !isCorner && world.getLightEmission(blockPos) < 10;

		return new NestBlockData(fullCoverage.get(), isCorner, isFloor, isCeiling, isWall,
				/* upCoverage */ upState.isSolidRender(world, upPos),
				/* downCoverage */ downState.isSolidRender(world, downPos),
				/* northCoverage */ northState.isSolidRender(world, northPos),
				/* southCoverage */ southState.isSolidRender(world, southPos),
				/* eastCoverage */ eastState.isSolidRender(world, eastPos),
				/* westCoverage */ westState.isSolidRender(world, westPos));
	}
}
