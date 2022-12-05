package mods.cybercat.gigeresque.common.tags;

import mods.cybercat.gigeresque.common.Gigeresque;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class GigTags {

	/* BLOCKS */
	public static final TagKey<Block> ALIEN_REPELLENTS = TagKey.create(Registries.BLOCK,
			new ResourceLocation(Gigeresque.MOD_ID, "alien_repellents"));
	public static final TagKey<Block> DESTRUCTIBLE_LIGHT = TagKey.create(Registries.BLOCK,
			new ResourceLocation(Gigeresque.MOD_ID, "destructible_light"));
	public static final TagKey<Block> ACID_RESISTANT = TagKey.create(Registries.BLOCK,
			new ResourceLocation(Gigeresque.MOD_ID, "acid_resistant"));
	public static final TagKey<Block> DUNGEON_BLOCKS = TagKey.create(Registries.BLOCK,
			new ResourceLocation(Gigeresque.MOD_ID, "dungeon_blocks"));
	public static final TagKey<Block> DUNGEON_STAIRS = TagKey.create(Registries.BLOCK,
			new ResourceLocation(Gigeresque.MOD_ID, "dungeon_stairs"));

	/* ITEMS */
	public static final TagKey<Item> BUSTER_FOOD = TagKey.create(Registries.ITEM,
			new ResourceLocation(Gigeresque.MOD_ID, "buster_food"));
}
