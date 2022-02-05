package com.bvanseg.gigeresque.common.block.tag

import com.bvanseg.gigeresque.common.Gigeresque
import net.fabricmc.fabric.api.tag.TagFactory
import net.minecraft.util.Identifier

/**
 * @author Boston Vanseghi
 */
object BlockTags {
    val ALIEN_REPELLENTS = TagFactory.BLOCK.create(Identifier(Gigeresque.MOD_ID, "alien_repellents"))
    val DESTRUCTIBLE_LIGHT = TagFactory.BLOCK.create(Identifier(Gigeresque.MOD_ID, "destructible_light"))
}