package mods.cybercat.gigeresque.common.util.nest;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.block.NestResinWebBlock;
import mods.cybercat.gigeresque.common.block.NestResinWebVariant;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NestBuildingHelper {
	private NestBuildingHelper() {
	}

	public static void tryBuildNestAround(AlienEntity alien) {
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				for (int y = -1; y <= 3; y++) {
					var blockPos = alien.getBlockPos().add(x, y, z);
					var nestBlockData = getNestBlockData(alien.world, blockPos);
					if (nestBlockData == null)
						continue;

					if (nestBlockData.isFloor()) {
						alien.world.setBlockState(blockPos, GIgBlocks.NEST_RESIN.getDefaultState());
					}

					if (nestBlockData.isCorner()) {
						alien.world.setBlockState(blockPos, GIgBlocks.NEST_RESIN_WEB_CROSS.getDefaultState());
					}

					if (nestBlockData.isWall() || nestBlockData.isCeiling()) {
						var nestResinWebState = GIgBlocks.NEST_RESIN_WEB.getDefaultState()
								.with(NestResinWebBlock.UP, nestBlockData.hasUpCoverage())
								.with(NestResinWebBlock.NORTH, nestBlockData.hasNorthCoverage())
								.with(NestResinWebBlock.SOUTH, nestBlockData.hasSouthCoverage())
								.with(NestResinWebBlock.EAST, nestBlockData.hasEastCoverage())
								.with(NestResinWebBlock.WEST, nestBlockData.hasWestCoverage())
								.with(NestResinWebBlock.VARIANTS, NestResinWebVariant.values()[new Random()
										.nextInt(NestResinWebVariant.values().length)]);
						alien.world.setBlockState(blockPos, nestResinWebState);
					}
				}
			}
		}
	}

	public static boolean isResinBlock(Block block) {
		return block == GIgBlocks.NEST_RESIN || block == GIgBlocks.NEST_RESIN_WEB || block == GIgBlocks.NEST_RESIN_WEB_CROSS
				|| block == GIgBlocks.NEST_RESIN_BLOCK;
	}

	private static NestBlockData getNestBlockData(World world, BlockPos blockPos) {
		var actualState = world.getBlockState(blockPos);
		var actualBlock = actualState.getBlock();

		if (isResinBlock(actualBlock))
			return null;

		var upPos = blockPos.up();
		var upState = world.getBlockState(upPos);

		var downPos = blockPos.down();
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

		AtomicInteger fullCoverage = new AtomicInteger();
		AtomicInteger horizontalCoverage = new AtomicInteger();
		AtomicInteger cornerCoverage = new AtomicInteger();

		blockStates.forEach(it -> {
			var isOpaqueCube = it.getSecond().isOpaqueFullCube(world, it.getFirst());

			if (isOpaqueCube) {
				if (!isResinBlock(it.getSecond().getBlock())) {
					if (it.getFirst() != upPos && it.getFirst() != downPos) {
						horizontalCoverage.getAndIncrement();
					}
					fullCoverage.getAndIncrement();
				} else {
					cornerCoverage.getAndIncrement();
				}
			}
		});

		var isFloor = actualState.isAir() && upState.isAir() && !isResinBlock(downState.getBlock())
				&& downState.isOpaqueFullCube(world, downPos);
		var isWall = !isFloor && actualState.isAir() && (upState.isAir() || isResinBlock(upState.getBlock()))
				&& (1 <= horizontalCoverage.get() && horizontalCoverage.get() <= 2);
		var isCorner = actualState.isAir() && (3 <= fullCoverage.get() && fullCoverage.get() <= 5);
		var isCeiling = actualState.isAir() && (downState.isAir() || isResinBlock(downState.getBlock()))
				&& upState.isOpaqueFullCube(world, downPos) && !isCorner;

		return new NestBlockData(fullCoverage.get(), isCorner, isFloor, isCeiling, isWall,
				/* upCoverage */ upState.isOpaqueFullCube(world, upPos),
				/* downCoverage */ downState.isOpaqueFullCube(world, downPos),
				/* northCoverage */ northState.isOpaqueFullCube(world, northPos),
				/* southCoverage */ southState.isOpaqueFullCube(world, southPos),
				/* eastCoverage */ eastState.isOpaqueFullCube(world, eastPos),
				/* westCoverage */ westState.isOpaqueFullCube(world, westPos));
	}
}
