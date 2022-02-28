package mods.cybercat.gigeresque.common.structures;

import mods.cybercat.gigeresque.common.Gigeresque;

import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class Structures {

	public static StructureFeature<StructurePoolFeatureConfig> GIG_DUNGEON = new GigDungeonStructure(
			StructurePoolFeatureConfig.CODEC);

	public static void setupAndRegisterStructureFeatures() {
		FabricStructureBuilder.create(new Identifier(Gigeresque.MOD_ID, "gig_dungeon"), GIG_DUNGEON)
				.step(GenerationStep.Feature.SURFACE_STRUCTURES).defaultConfig(new StructureConfig(80, 20, 1234567890))
				.adjustsSurface().register();
	}

}
