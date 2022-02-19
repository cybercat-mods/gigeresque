//package com.bvanseg.gigeresque.client.entity.render
//
//import com.bvanseg.gigeresque.common.entity.Entities
//import com.bvanseg.gigeresque.common.util.GigeresqueInitializer
//import com.bvanseg.gigeresque.common.util.initializingBlock
//import net.fabricmc.api.EnvType
//import net.fabricmc.api.Environment
//import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
//
//
///**
// * @author Boston Vanseghi
// */
//@Environment(EnvType.CLIENT)
//object EntityRenderers : GigeresqueInitializer {
//    override fun initialize() = initializingBlock("EntityRenderers") {
//        EntityRendererRegistry.register(Entities.ALIEN, ::AlienEntityRenderer)
//        EntityRendererRegistry.register(Entities.AQUATIC_ALIEN, ::AquaticAlienEntityRenderer)
//        EntityRendererRegistry.register(Entities.AQUATIC_CHESTBURSTER, ::AquaticChestbursterEntityRenderer)
//        EntityRendererRegistry.register(Entities.CHESTBURSTER, ::ChestbursterEntityRenderer)
//        EntityRendererRegistry.register(Entities.EGG, ::EggEntityRenderer)
//        EntityRendererRegistry.register(Entities.FACEHUGGER, ::FacehuggerEntityRenderer)
//        EntityRendererRegistry.register(Entities.RUNNER_ALIEN, ::RunnerAlienEntityRenderer)
//        EntityRendererRegistry.register(Entities.RUNNERBURSTER, ::RunnerbursterEntityRenderer)
//    }
//}