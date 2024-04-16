package mods.cybercat.gigeresque.client;

import mods.cybercat.gigeresque.client.block.BlockRenderLayers;
import mods.cybercat.gigeresque.client.entity.render.GigEntityRenderers;
import mods.cybercat.gigeresque.client.fluid.render.FluidRenderHandlers;
import mods.cybercat.gigeresque.client.particle.Particles;
import net.fabricmc.api.ClientModInitializer;

public class GigeresqueClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        new BlockRenderLayers().initialize();
        new FluidRenderHandlers().initialize();
        new GigEntityRenderers().initialize();
        new Particles().initialize();
    }
}
