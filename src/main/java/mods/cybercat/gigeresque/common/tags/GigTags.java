package mods.cybercat.gigeresque.common.tags;

import mods.cybercat.gigeresque.common.Gigeresque;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class GigTags {

	/* BLOCKS */
	public static final TagKey<Block> ALIEN_REPELLENTS = TagKey.create(Registry.BLOCK_REGISTRY,
			new ResourceLocation(Gigeresque.MOD_ID, "alien_repellents"));
	public static final TagKey<Block> DESTRUCTIBLE_LIGHT = TagKey.create(Registry.BLOCK_REGISTRY,
			new ResourceLocation(Gigeresque.MOD_ID, "destructible_light"));
	public static final TagKey<Block> ACID_RESISTANT = TagKey.create(Registry.BLOCK_REGISTRY,
			new ResourceLocation(Gigeresque.MOD_ID, "acid_resistant"));
	public static final TagKey<Block> DUNGEON_BLOCKS = TagKey.create(Registry.BLOCK_REGISTRY,
			new ResourceLocation(Gigeresque.MOD_ID, "dungeon_blocks"));
	public static final TagKey<Block> DUNGEON_STAIRS = TagKey.create(Registry.BLOCK_REGISTRY,
			new ResourceLocation(Gigeresque.MOD_ID, "dungeon_stairs"));

	/* ITEMS */
	public static final TagKey<Item> BUSTER_FOOD = TagKey.create(Registry.ITEM_REGISTRY,
			new ResourceLocation(Gigeresque.MOD_ID, "buster_food"));
}
