//package com.bvanseg.gigeresque.client
//
//import com.bvanseg.gigeresque.client.block.BlockRenderLayers
//import com.bvanseg.gigeresque.client.entity.render.EntityRenderers
//import com.bvanseg.gigeresque.client.fluid.render.FluidRenderHandlers
//import com.bvanseg.gigeresque.client.particle.Particles
//import com.bvanseg.gigeresque.common.util.initializingBlock
//import net.fabricmc.api.ClientModInitializer
//import net.fabricmc.api.EnvType
//import net.fabricmc.api.Environment
//
///**
// * @author Boston Vanseghi
// */
//@Environment(EnvType.CLIENT)
//object GigeresqueClient : ClientModInitializer {
//
//    override fun onInitializeClient() = initializingBlock("GigeresqueClient") {
//        BlockRenderLayers.initialize()
//        FluidRenderHandlers.initialize()
//        EntityRenderers.initialize()
//        Particles.initialize()
//    }
//}