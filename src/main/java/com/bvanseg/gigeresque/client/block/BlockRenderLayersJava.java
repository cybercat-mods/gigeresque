package com.bvanseg.gigeresque.client.block;

import com.bvanseg.gigeresque.common.block.Blocks;
import com.bvanseg.gigeresque.common.block.BlocksJava;
import com.bvanseg.gigeresque.common.util.GigeresqueInitializerJava;
import com.bvanseg.gigeresque.common.util.InitializationTimer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class BlockRenderLayersJava implements GigeresqueInitializerJava {
    @Override
    public void initialize() {
        InitializationTimer.initializingBlock("BlockRenderLayers", () -> {
            BlockRenderLayerMap.INSTANCE.putBlock(BlocksJava.NEST_RESIN_WEB, RenderLayer.getCutout());
            BlockRenderLayerMap.INSTANCE.putBlock(BlocksJava.NEST_RESIN_WEB_CROSS, RenderLayer.getCutout());
        });
    }
}
