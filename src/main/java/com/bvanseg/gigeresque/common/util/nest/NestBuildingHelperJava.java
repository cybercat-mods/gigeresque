package com.bvanseg.gigeresque.common.util.nest;

import com.bvanseg.gigeresque.common.block.BlocksJava;
import com.bvanseg.gigeresque.common.block.NestResinWebBlockJava;
import com.bvanseg.gigeresque.common.block.NestResinWebVariantJava;
import com.bvanseg.gigeresque.common.entity.AlienEntityJava;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class NestBuildingHelperJava {
    private NestBuildingHelperJava() {
    }

    public static void tryBuildNestAround(AlienEntityJava alien) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = -1; y <= 3; y++) {
                    var blockPos = alien.getBlockPos().add(x, y, z);
                    var nestBlockData = getNestBlockData(alien.world, blockPos);
                    if (nestBlockData == null) continue;

                    if (nestBlockData.isFloor()) {
                        alien.world.setBlockState(blockPos, BlocksJava.NEST_RESIN.getDefaultState());
                    }

                    if (nestBlockData.isCorner()) {
                        alien.world.setBlockState(blockPos, BlocksJava.NEST_RESIN_WEB_CROSS.getDefaultState());
                    }

                    if (nestBlockData.isWall() || nestBlockData.isCeiling()) {
                        var nestResinWebState = BlocksJava.NEST_RESIN_WEB.getDefaultState()
                                .with(NestResinWebBlockJava.UP, nestBlockData.hasUpCoverage())
                                .with(NestResinWebBlockJava.NORTH, nestBlockData.hasNorthCoverage())
                                .with(NestResinWebBlockJava.SOUTH, nestBlockData.hasSouthCoverage())
                                .with(NestResinWebBlockJava.EAST, nestBlockData.hasEastCoverage())
                                .with(NestResinWebBlockJava.WEST, nestBlockData.hasWestCoverage())
                                .with(NestResinWebBlockJava.VARIANTS, NestResinWebVariantJava.values()[new Random().nextInt(NestResinWebVariantJava.values().length)]);
                        alien.world.setBlockState(blockPos, nestResinWebState);
                    }
                }
            }
        }
    }

    public static boolean isResinBlock(Block block) {
        return block == BlocksJava.NEST_RESIN ||
                block == BlocksJava.NEST_RESIN_WEB ||
                block == BlocksJava.NEST_RESIN_WEB_CROSS ||
                block == BlocksJava.NEST_RESIN_BLOCK;
    }

    private static NestBlockDataJava getNestBlockData(World world, BlockPos blockPos) {
        var actualState = world.getBlockState(blockPos);
        var actualBlock = actualState.getBlock();

        if (isResinBlock(actualBlock)) return null;

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

        var blockStates = List.of(
                Pair.of(upPos, upState),
                Pair.of(downPos, downState),
                Pair.of(northPos, northState),
                Pair.of(southPos, southState),
                Pair.of(eastPos, eastState),
                Pair.of(westPos, westState)
        );

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

        var isFloor =
                actualState.isAir() && upState.isAir() && !isResinBlock(downState.getBlock()) && downState.isOpaqueFullCube(
                        world,
                        downPos
                );
        var isWall = !isFloor && actualState.isAir() && (upState.isAir() || isResinBlock(upState.getBlock())) && (1 <= horizontalCoverage.get() && horizontalCoverage.get() <= 2);
        var isCorner = actualState.isAir() && (3 <= fullCoverage.get() && fullCoverage.get() <= 5);
        var isCeiling =
                actualState.isAir() && (downState.isAir() || isResinBlock(downState.getBlock())) && upState.isOpaqueFullCube(
                        world,
                        downPos
                ) && !isCorner;

        return new NestBlockDataJava(
                fullCoverage.get(),
                isCorner,
                isFloor,
                isCeiling,
                isWall,
                /*upCoverage*/ upState.isOpaqueFullCube(world, upPos),
                /*downCoverage*/ downState.isOpaqueFullCube(world, downPos),
                /*northCoverage*/ northState.isOpaqueFullCube(world, northPos),
                /*southCoverage*/ southState.isOpaqueFullCube(world, southPos),
                /*eastCoverage*/ eastState.isOpaqueFullCube(world, eastPos),
                /*westCoverage*/ westState.isOpaqueFullCube(world, westPos)
        );
    }
}
