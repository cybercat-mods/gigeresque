package mods.cybercat.gigeresque.client.block;

import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

public record BlockRenderLayers() implements GigeresqueInitializer {
	@Override
	public void initialize() {
		BlockRenderLayerMap.INSTANCE.putBlock(GIgBlocks.NEST_RESIN_WEB, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(GIgBlocks.NEST_RESIN_WEB_CROSS, RenderType.translucent());
	}
}
