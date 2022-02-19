package com.bvanseg.gigeresque.common.block.tag;

import com.bvanseg.gigeresque.common.Gigeresque;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class BlockTags {
    private BlockTags() {
    }

    public static final Tag.Identified<Block> ALIEN_REPELLENTS = TagFactory.BLOCK.create(new Identifier(Gigeresque.MOD_ID, "alien_repellents"));
    public static final Tag.Identified<Block> DESTRUCTIBLE_LIGHT = TagFactory.BLOCK.create(new Identifier(Gigeresque.MOD_ID, "destructible_light"));
}
