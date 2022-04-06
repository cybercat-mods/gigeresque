package mods.cybercat.gigeresque.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.data.handler.TrackedDataHandlers;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import mods.cybercat.gigeresque.common.item.GigItems;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.structures.Structures;
import net.fabricmc.api.ModInitializer;
import software.bernie.geckolib3.GeckoLib;

public class Gigeresque implements ModInitializer {
	public static GigeresqueConfig config;
	public static final Logger LOGGER = LoggerFactory.getLogger(Gigeresque.class);
	public static final String MOD_ID = "gigeresque";

	@Override
	public void onInitialize() {
		AutoConfig.register(GigeresqueConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(GigeresqueConfig.class).getConfig();

		GeckoLib.initialize();

		GigItems.getInstance().initialize();
		GIgBlocks.getInstance().initialize();
		GigFluids.getInstance().initialize();
		GigSounds.getInstance().initialize();
		GigStatusEffects.getInstance().initialize();
		TrackedDataHandlers.getInstance().initialize();
		Entities.getInstance().initialize();
		Structures.setupAndRegisterStructureFeatures();
	}
}
