//package com.bvanseg.gigeresque.client.block
//
//import com.bvanseg.gigeresque.common.block.Blocks
//import com.bvanseg.gigeresque.common.util.GigeresqueInitializer
//import com.bvanseg.gigeresque.common.util.initializingBlock
//import net.fabricmc.api.EnvType
//import net.fabricmc.api.Environment
//import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
//import net.minecraft.client.render.RenderLayer
//
///**
// * @author Boston Vanseghi
// */
//@Environment(EnvType.CLIENT)
//object BlockRenderLayers : GigeresqueInitializer {
//    override fun initialize() = initializingBlock("BlockRenderLayers") {
//        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.NEST_RESIN_WEB, RenderLayer.getCutout())
//        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.NEST_RESIN_WEB_CROSS, RenderLayer.getCutout())
//    }
//}