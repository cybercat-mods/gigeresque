package mods.cybercat.gigeresque;

import mod.azure.azurelib.AzureLibMod;
import mod.azure.azurelib.common.config.format.ConfigFormats;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mods.cybercat.gigeresque.client.particle.GigParticles;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import mods.cybercat.gigeresque.common.item.GigItems;
import mods.cybercat.gigeresque.common.item.group.GigItemGroups;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;

public record CommonMod() {

    public static final String MOD_ID = "gigeresque";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static GigeresqueConfig config;

    public static void initRegistries() {
        config = AzureLibMod.registerConfig(GigeresqueConfig.class, ConfigFormats.json()).getConfigInstance();
        GigFluids.initialize();
        GigEntities.initialize();
        GigBlocks.initialize();
        GigItemGroups.initialize();
        GigItems.initialize();
        GigSounds.initialize();
        GigStatusEffects.initialize();
        GigParticles.initialize();
    }
}
