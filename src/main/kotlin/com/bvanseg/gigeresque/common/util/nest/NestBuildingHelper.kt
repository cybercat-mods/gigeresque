package com.bvanseg.gigeresque.common.util.nest

import com.bvanseg.gigeresque.common.block.Blocks
import com.bvanseg.gigeresque.common.block.NestResinWebBlock
import com.bvanseg.gigeresque.common.block.NestResinWebVariant
import com.bvanseg.gigeresque.common.entity.AlienEntity
import net.minecraft.block.Block
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * @author Boston Vanseghi
 */
object NestBuildingHelper {

    fun tryBuildNestAround(alien: AlienEntity) {
        for (x in -1..1) {
            for (z in -1..1) {
                for (y in -1..3) {
                    val blockPos = alien.blockPos.add(x, y, z)
                    val nestBlockData = getNestBlockData(alien.world, blockPos) ?: continue

                    if (nestBlockData.isFloor) {
                        alien.world.setBlockState(blockPos, Blocks.NEST_RESIN.defaultState)
                    }

                    if (nestBlockData.isCorner) {
                        alien.world.setBlockState(blockPos, Blocks.NEST_RESIN_WEB_CROSS.defaultState)
                    }

                    if (nestBlockData.isWall || nestBlockData.isCeiling) {
                        val nestResinWebState = Blocks.NEST_RESIN_WEB.defaultState
                            .with(NestResinWebBlock.UP, nestBlockData.upCoverage)
                            .with(NestResinWebBlock.NORTH, nestBlockData.northCoverage)
                            .with(NestResinWebBlock.SOUTH, nestBlockData.southCoverage)
                            .with(NestResinWebBlock.EAST, nestBlockData.eastCoverage)
                            .with(NestResinWebBlock.WEST, nestBlockData.westCoverage)
                            .with(NestResinWebBlock.VARIANTS, NestResinWebVariant.values().random())
                        alien.world.setBlockState(blockPos, nestResinWebState)
                    }
                }
            }
        }
    }

    fun isResinBlock(block: Block): Boolean = block == Blocks.NEST_RESIN ||
            block == Blocks.NEST_RESIN_WEB ||
            block == Blocks.NEST_RESIN_WEB_CROSS ||
            block == Blocks.NEST_RESIN_BLOCK

    private fun getNestBlockData(world: World, blockPos: BlockPos): NestBlockData? {
        val actualState = world.getBlockState(blockPos)
        val actualBlock = actualState.block

        if (isResinBlock(actualBlock)) return null

        val upPos = blockPos.up()
        val upState = world.getBlockState(upPos)

        val downPos = blockPos.down()
        val downState = world.getBlockState(downPos)

        val northPos = blockPos.north()
        val northState = world.getBlockState(northPos)

        val southPos = blockPos.south()
        val southState = world.getBlockState(southPos)

        val eastPos = blockPos.east()
        val eastState = world.getBlockState(eastPos)

        val westPos = blockPos.west()
        val westState = world.getBlockState(westPos)

        val blockStates = listOf(
            upPos to upState,
            downPos to downState,
            northPos to northState,
            southPos to southState,
            eastPos to eastState,
            westPos to westState
        )

        var fullCoverage = 0
        var horizontalCoverage = 0
        var cornerCoverage = 0

        blockStates.forEach {
            val isOpaqueCube = it.second.isOpaqueFullCube(world, it.first)

            if (isOpaqueCube) {
                if (!isResinBlock(it.second.block)) {
                    if (it.first != upPos && it.first != downPos) {
                        horizontalCoverage++
                    }
                    fullCoverage++
                } else {
                    cornerCoverage++
                }
            }
        }

        val isFloor =
            actualState.isAir && upState.isAir && !isResinBlock(downState.block) && downState.isOpaqueFullCube(
                world,
                downPos
            )
        val isWall =
            !isFloor && actualState.isAir && (upState.isAir || isResinBlock(upState.block)) && horizontalCoverage in 1..2
        val isCorner = actualState.isAir && fullCoverage in 3..5
        val isCeiling =
            actualState.isAir && (downState.isAir || isResinBlock(downState.block)) && upState.isOpaqueFullCube(
                world,
                downPos
            ) && !isCorner

        return NestBlockData(
            coverage = fullCoverage,
            isCorner = isCorner,
            isFloor = isFloor,
            isCeiling = isCeiling,
            isWall = isWall,
            upCoverage = upState.isOpaqueFullCube(world, upPos),
            downCoverage = downState.isOpaqueFullCube(world, downPos),
            northCoverage = northState.isOpaqueFullCube(world, northPos),
            southCoverage = southState.isOpaqueFullCube(world, southPos),
            eastCoverage = eastState.isOpaqueFullCube(world, eastPos),
            westCoverage = westState.isOpaqueFullCube(world, westPos)
        )
    }
}