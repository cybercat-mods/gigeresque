package com.bvanseg.gigeresque.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;

public class CustomStairsBlock extends StairsBlock {
	public CustomStairsBlock(BlockState baseBlockState, Settings settings) {
		super(baseBlockState, settings);
	}

	public CustomStairsBlock(Block block, Settings settings) {
		this(block.getDefaultState(), settings);
	}

	public CustomStairsBlock(Block block) {
		this(block, Settings.copy(block));
	}
}
