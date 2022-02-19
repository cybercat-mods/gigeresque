package com.bvanseg.gigeresque.client.block;

import com.bvanseg.gigeresque.common.block.Blocks;
import com.bvanseg.gigeresque.common.util.GigeresqueInitializer;
import com.bvanseg.gigeresque.common.util.InitializationTimer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class BlockRenderLayers implements GigeresqueInitializer {
    @Override
    public void initialize() {
        InitializationTimer.initializingBlock("BlockRenderLayers", () -> {
            BlockRenderLayerMap.INSTANCE.putBlock(Blocks.NEST_RESIN_WEB, RenderLayer.getCutout());
            BlockRenderLayerMap.INSTANCE.putBlock(Blocks.NEST_RESIN_WEB_CROSS, RenderLayer.getCutout());
        });
    }
}
