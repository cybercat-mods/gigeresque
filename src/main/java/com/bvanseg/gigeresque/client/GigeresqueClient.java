package com.bvanseg.gigeresque.client;

import com.bvanseg.gigeresque.client.block.BlockRenderLayers;
import com.bvanseg.gigeresque.client.entity.render.EntityRenderers;
import com.bvanseg.gigeresque.client.fluid.render.FluidRenderHandlers;
import com.bvanseg.gigeresque.client.particle.Particles;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class GigeresqueClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		new BlockRenderLayers().initialize();
		new FluidRenderHandlers().initialize();
		new EntityRenderers().initialize();
		new Particles().initialize();
	}
}
