package com.bvanseg.gigeresque.common;

import com.bvanseg.gigeresque.common.block.Blocks;
import com.bvanseg.gigeresque.common.config.GigeresqueConfig;
import com.bvanseg.gigeresque.common.data.handler.TrackedDataHandlers;
import com.bvanseg.gigeresque.common.entity.Entities;
import com.bvanseg.gigeresque.common.fluid.Fluids;
import com.bvanseg.gigeresque.common.item.Items;
import com.bvanseg.gigeresque.common.sound.Sounds;
import com.bvanseg.gigeresque.common.status.effect.StatusEffects;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    }
}
