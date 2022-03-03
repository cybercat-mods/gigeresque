package mods.cybercat.gigeresque.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import mods.cybercat.gigeresque.common.block.Blocks;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.data.handler.TrackedDataHandlers;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.fluid.Fluids;
import mods.cybercat.gigeresque.common.item.Items;
import mods.cybercat.gigeresque.common.sound.Sounds;
import mods.cybercat.gigeresque.common.status.effect.StatusEffects;
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

		Items.getInstance().initialize();
		Blocks.getInstance().initialize();
		Fluids.getInstance().initialize();
		Sounds.getInstance().initialize();
		StatusEffects.getInstance().initialize();
		TrackedDataHandlers.getInstance().initialize();
		Entities.getInstance().initialize();
		Structures.setupAndRegisterStructureFeatures();
	}
}
