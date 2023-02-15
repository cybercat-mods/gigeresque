package mods.cybercat.gigeresque.common;

import eu.midnightdust.lib.config.MidnightConfig;
import mod.azure.azurelib.AzureLib;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.data.handler.TrackedDataHandlers;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.ai.GigMemoryTypes;
import mods.cybercat.gigeresque.common.entity.ai.GigSensors;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import mods.cybercat.gigeresque.common.item.GigItems;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.structures.GigStructures;
import mods.cybercat.gigeresque.common.util.MobSpawn;
import net.fabricmc.api.ModInitializer;

public class Gigeresque implements ModInitializer {
	public static GigeresqueConfig config;
	public static final String MOD_ID = "gigeresque";

	@Override
	public void onInitialize() {
		MidnightConfig.init(MOD_ID, GigeresqueConfig.class);

		GigItems.init();
		GigMemoryTypes.init();
		GigSensors.init();
		GIgBlocks.getInstance().initialize();
		GigFluids.getInstance().initialize();
		GigSounds.getInstance().initialize();
		GigStatusEffects.getInstance().initialize();
		TrackedDataHandlers.getInstance().initialize();
		Entities.getInstance().initialize();
		MobSpawn.addSpawnEntries();
		GigStructures.registerStructureFeatures();
		AzureLib.initialize();
	}
}
