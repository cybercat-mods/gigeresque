package com.bvanseg.gigeresque.common;

import com.bvanseg.gigeresque.common.config.GigeresqueConfigJava;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GigeresqueJava implements ModInitializer {
//    GigeresqueConfig
    public static GigeresqueConfigJava config;
    public static final Logger LOGGER = LoggerFactory.getLogger(GigeresqueJava.class);
    public static final String MOD_ID = "gigeresque";

    @Override
    public void onInitialize() {
//        AutoConfig.register(GigeresqueConfigJava)
    }
}
