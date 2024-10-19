package mods.cybercat.gigeresque.client.block;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;

public record BlockRenderLayers() implements GigeresqueInitializer {

    @Override
    public void initialize() {
        BlockRenderLayerMap.INSTANCE.putBlock(GigBlocks.NEST_RESIN_WEB, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GigBlocks.NEST_RESIN_WEB_CROSS, RenderType.translucent());
    }
}
