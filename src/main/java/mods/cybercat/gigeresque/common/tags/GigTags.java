package mods.cybercat.gigeresque.common.tags;

import mods.cybercat.gigeresque.Constants;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;

public class GigTags {

	/* BLOCKS */
	public static final TagKey<Block> ALIEN_REPELLENTS = TagKey.create(Registry.BLOCK_REGISTRY,
			Constants.modResource("alien_repellents"));
	public static final TagKey<Block> DESTRUCTIBLE_LIGHT = TagKey.create(Registry.BLOCK_REGISTRY,
			Constants.modResource("destructible_light"));
	public static final TagKey<Block> ACID_RESISTANT = TagKey.create(Registry.BLOCK_REGISTRY,
			Constants.modResource("acid_resistant"));
	public static final TagKey<Block> DUNGEON_BLOCKS = TagKey.create(Registry.BLOCK_REGISTRY,
			Constants.modResource("dungeon_blocks"));
	public static final TagKey<Block> DUNGEON_STAIRS = TagKey.create(Registry.BLOCK_REGISTRY,
			Constants.modResource("dungeon_stairs"));

	/* ITEMS */
	public static final TagKey<Item> BUSTER_FOOD = TagKey.create(Registry.ITEM_REGISTRY, Constants.modResource("buster_food"));

	/* DUNGEONS */
	public static final TagKey<Structure> GIG_EXPLORER_MAPS = TagKey.create(Registry.STRUCTURE_REGISTRY,
			Constants.modResource("gig_explorer_maps"));
}
