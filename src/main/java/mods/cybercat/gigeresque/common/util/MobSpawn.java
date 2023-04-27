package mods.cybercat.gigeresque.common.util;

import java.util.List;

import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
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
		BiomeModifications.addSpawn(BiomeSelectors.all().and(context -> parseBiomes(GigeresqueConfig.alienegg_biomes, context)), MobCategory.MONSTER, Entities.EGG, GigeresqueConfig.alienegg_spawn_weight, GigeresqueConfig.alienegg_min_group, GigeresqueConfig.alienegg_max_group);
		SpawnPlacements.register(Entities.EGG, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AlienEggEntity::canSpawn);
	}

	private static boolean parseBiomes(List<String> biomes, BiomeSelectionContext biomeContext) {
		return biomes.contains(biomeContext.getBiomeKey().location().toString());
	}
}
