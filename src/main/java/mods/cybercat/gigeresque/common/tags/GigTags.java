package mods.cybercat.gigeresque.common.tags;

import mods.cybercat.gigeresque.Constants;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;

public class GigTags {

	/* BLOCKS */
	public static final TagKey<Block> ALIEN_REPELLENTS = TagKey.create(Registry.BLOCK_REGISTRY, Constants.modResource("alien_repellents"));
	public static final TagKey<Block> DESTRUCTIBLE_LIGHT = TagKey.create(Registry.BLOCK_REGISTRY, Constants.modResource("destructible_light"));
	public static final TagKey<Block> ACID_RESISTANT = TagKey.create(Registry.BLOCK_REGISTRY, Constants.modResource("acid_resistant"));
	public static final TagKey<Block> DUNGEON_BLOCKS = TagKey.create(Registry.BLOCK_REGISTRY, Constants.modResource("dungeon_blocks"));
	public static final TagKey<Block> DUNGEON_STAIRS = TagKey.create(Registry.BLOCK_REGISTRY, Constants.modResource("dungeon_stairs"));
	public static final TagKey<Block> WEAK_BLOCKS = TagKey.create(Registry.BLOCK_REGISTRY, Constants.modResource("weak_block"));

	/* DUNGEONS */
	public static final TagKey<Structure> GIG_EXPLORER_MAPS = TagKey.create(Registry.STRUCTURE_REGISTRY, Constants.modResource("gig_explorer_maps"));

	/* MOBS */
	public static final TagKey<EntityType<?>> AQUATIC_HOSTS = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, Constants.modResource("aquaticalienhost"));
	public static final TagKey<EntityType<?>> CLASSIC_HOSTS = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, Constants.modResource("classicalienhost"));
	public static final TagKey<EntityType<?>> RUNNER_HOSTS = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, Constants.modResource("runnerhost"));
	public static final TagKey<EntityType<?>> MUTANT_SMALL_HOSTS = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, Constants.modResource("smallmutanthost"));
	public static final TagKey<EntityType<?>> MUTANT_LARGE_HOSTS = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, Constants.modResource("largemutanthost"));
	public static final TagKey<EntityType<?>> NEOHOST = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, Constants.modResource("neohost"));
	public static final TagKey<EntityType<?>> DNAIMMUNE = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, Constants.modResource("dnaimmune"));
	public static final TagKey<EntityType<?>> FACEHUGGER_BLACKLIST = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, Constants.modResource("facehuggerblacklist"));
	
	/* SPAWN BIOMES */
	public static final TagKey<Biome> EGGSPAWN_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, Constants.modResource("eggbiomes"));
}
