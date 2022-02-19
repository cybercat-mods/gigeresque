package com.bvanseg.gigeresque.client;

import com.bvanseg.gigeresque.client.block.BlockRenderLayersJava;
import com.bvanseg.gigeresque.client.entity.render.EntityRenderersJava;
import com.bvanseg.gigeresque.client.fluid.render.FluidRenderHandlersJava;
import com.bvanseg.gigeresque.client.particle.ParticlesJava;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class GigeresqueClientJava implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        new BlockRenderLayersJava().initialize();
        new FluidRenderHandlersJava().initialize();
        new EntityRenderersJava().initialize();
        new ParticlesJava().initialize();
    }
}
