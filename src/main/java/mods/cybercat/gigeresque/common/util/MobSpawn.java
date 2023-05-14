package mods.cybercat.gigeresque.common.util;

import java.util.Arrays;
import java.util.List;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public class MobSpawn {

	public static void addSpawnEntries() {
		BiomeModifications.addSpawn(BiomeSelectors.all().and(context -> parseBiomes(Gigeresque.config.alienegg_biomes, context)), MobCategory.MONSTER, Entities.EGG, Gigeresque.config.alienegg_spawn_weight, Gigeresque.config.alienegg_min_group, Gigeresque.config.alienegg_max_group);
		SpawnPlacements.register(Entities.EGG, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AlienEggEntity::canSpawn);
	}

	private static boolean parseBiomes(String[] biomes, BiomeSelectionContext biomeContext) {
		List<String> biomelist = Arrays.asList(biomes);
		return biomelist.contains(biomeContext.getBiomeKey().location().toString());
	}
}
