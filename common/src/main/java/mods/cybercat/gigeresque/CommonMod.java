package mods.cybercat.gigeresque;

import mod.azure.azurelib.common.internal.common.AzureLibMod;
import mod.azure.azurelib.common.internal.common.config.format.ConfigFormats;
import mods.cybercat.gigeresque.client.particle.GigParticles;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.ai.GigMemoryTypes;
import mods.cybercat.gigeresque.common.entity.ai.GigSensors;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import mods.cybercat.gigeresque.common.item.GigItems;
import mods.cybercat.gigeresque.common.item.group.GigItemGroups;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.structures.GigStructures;

public record CommonMod() {
    public static final String MOD_ID = "gigeresque";
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
        GigStructures.initialize();
        GigMemoryTypes.initialize();
        GigSensors.initialize();
        GigParticles.initialize();
    }
}
