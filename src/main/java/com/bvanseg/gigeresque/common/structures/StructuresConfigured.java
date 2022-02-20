package com.bvanseg.gigeresque.common.structures;

import com.bvanseg.gigeresque.common.Gigeresque;

import net.minecraft.structure.PlainsVillageData;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class StructuresConfigured {

	public static ConfiguredStructureFeature<?, ?> CONFIGURED_GIG_DUNGEON = Structures.GIG_DUNGEON
			.configure(new StructurePoolFeatureConfig(() -> PlainsVillageData.STRUCTURE_POOLS, 0));

	public static void registerConfiguredStructures() {
		Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
		Registry.register(registry, new Identifier(Gigeresque.MOD_ID, "configured_gig_dungeon"),
				CONFIGURED_GIG_DUNGEON);
	}
}
