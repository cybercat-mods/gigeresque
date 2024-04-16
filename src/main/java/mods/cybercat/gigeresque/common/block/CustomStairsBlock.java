package mods.cybercat.gigeresque.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CustomStairsBlock extends StairBlock {
    public CustomStairsBlock(BlockState baseBlockState, Properties settings) {
        super(baseBlockState, settings);
    }

    public CustomStairsBlock(Block block, Properties settings) {
        this(block.defaultBlockState(), settings);
    }

    public CustomStairsBlock(Block block) {
        this(block, Properties.ofFullCopy(block));
    }
}
