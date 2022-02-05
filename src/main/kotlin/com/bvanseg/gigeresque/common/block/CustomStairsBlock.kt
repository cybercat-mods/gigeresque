package com.bvanseg.gigeresque.common.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.StairsBlock

/**
 * @author Boston Vanseghi
 */
class CustomStairsBlock(baseBlockState: BlockState, settings: Settings) : StairsBlock(baseBlockState, settings) {
    constructor(block: Block, settings: Settings) : this(block.defaultState, settings)
    constructor(block: Block) : this(block.defaultState, Settings.copy(block))
}