package mods.cybercat.gigeresque.common;

import mod.azure.azurelib.AzureLibMod;
import mod.azure.azurelib.config.format.ConfigFormats;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.data.handler.TrackedDataHandlers;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.ai.GigMemoryTypes;
import mods.cybercat.gigeresque.common.entity.ai.GigSensors;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import mods.cybercat.gigeresque.common.item.GigItems;
import mods.cybercat.gigeresque.common.item.group.GigItemGroups;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.structures.GigStructures;
import mods.cybercat.gigeresque.common.util.GigVillagerTrades;
import mods.cybercat.gigeresque.common.util.MobSpawn;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;

public class Gigeresque implements ModInitializer {
	public static GigeresqueConfig config;
	public static final String MOD_ID = "gigeresque";

	@Override
	public void onInitialize() {
		config = AzureLibMod.registerConfig(GigeresqueConfig.class, ConfigFormats.json()).getConfigInstance();

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
		ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> GigVillagerTrades.addTrades());
		GigItemGroups.init();;
		FlammableBlockRegistry.getDefaultInstance().add(GIgBlocks.NEST_RESIN_BLOCK, 20, 5);
		FlammableBlockRegistry.getDefaultInstance().add(GIgBlocks.NEST_RESIN, 20, 5);
		FlammableBlockRegistry.getDefaultInstance().add(GIgBlocks.NEST_RESIN_WEB, 20, 5);
		FlammableBlockRegistry.getDefaultInstance().add(GIgBlocks.NEST_RESIN_WEB_CROSS, 20, 5);
	}
}
