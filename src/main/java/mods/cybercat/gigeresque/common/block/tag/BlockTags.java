package mods.cybercat.gigeresque.common.block.tag;

import mods.cybercat.gigeresque.common.Gigeresque;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockTags {
	private BlockTags() {
	}

	public static final TagKey<Block> ALIEN_REPELLENTS = TagKey.of(Registry.BLOCK_KEY,
			new Identifier(Gigeresque.MOD_ID, "alien_repellents"));
	public static final TagKey<Block> DESTRUCTIBLE_LIGHT = TagKey.of(Registry.BLOCK_KEY,
			new Identifier(Gigeresque.MOD_ID, "destructible_light"));
}
