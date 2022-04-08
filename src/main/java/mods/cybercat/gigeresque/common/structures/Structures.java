package mods.cybercat.gigeresque.common.structures;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.mixins.common.StructureFeatureAccessor;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.StructureFeature;

public class Structures {

	public static StructureFeature<?> GIG_DUNGEON = new GigDungeonStructure();

	public static void setupAndRegisterStructureFeatures() {
		StructureFeatureAccessor.callRegister(Gigeresque.MOD_ID + ":gig_dungeon", GIG_DUNGEON,
				GenerationStep.Feature.UNDERGROUND_STRUCTURES);
	}

}
