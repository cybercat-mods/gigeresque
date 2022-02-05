package com.bvanseg.gigeresque.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;

public class CustomStairsBlockJava extends StairsBlock {
    public CustomStairsBlockJava(BlockState baseBlockState, Settings settings) {
        super(baseBlockState, settings);
    }
    public CustomStairsBlockJava(Block block, Settings settings) {
        this(block.getDefaultState(), settings);
    }
    public CustomStairsBlockJava(Block block) {
        this(block, Settings.copy(block));
    }
}
