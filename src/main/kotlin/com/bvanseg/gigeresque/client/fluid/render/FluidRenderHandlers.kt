//package com.bvanseg.gigeresque.client.fluid.render
//
//import com.bvanseg.gigeresque.common.Gigeresque
//import com.bvanseg.gigeresque.common.fluid.Fluids
//import com.bvanseg.gigeresque.common.util.GigeresqueInitializer
//import com.bvanseg.gigeresque.common.util.initializingBlock
//import net.fabricmc.api.EnvType
//import net.fabricmc.api.Environment
//import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
//import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler
//import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
//import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback
//import net.fabricmc.fabric.api.resource.ResourceManagerHelper
//import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
//import net.minecraft.client.MinecraftClient
//import net.minecraft.client.render.RenderLayer
//import net.minecraft.client.texture.Sprite
//import net.minecraft.client.texture.SpriteAtlasTexture
//import net.minecraft.fluid.Fluid
//import net.minecraft.resource.ResourceManager
//import net.minecraft.resource.ResourceType
//import net.minecraft.util.Identifier
//import net.minecraft.util.registry.Registry
//
//
///**
// * @author Boston Vanseghi
// */
//@Environment(EnvType.CLIENT)
//object FluidRenderHandlers : GigeresqueInitializer {
//
//    override fun initialize() = initializingBlock("FluidRenderHandlers") {
//        setupFluidRendering(
//            Fluids.BLACK_FLUID_STILL,
//            Fluids.BLACK_FLUID_FLOWING,
//            Identifier(Gigeresque.MOD_ID, "black_fluid"),
//        )
//        BlockRenderLayerMap.INSTANCE.putFluids(
//            RenderLayer.getSolid(),
//            Fluids.BLACK_FLUID_STILL,
//            Fluids.BLACK_FLUID_FLOWING,
//        )
//    }
//
//    private fun setupFluidRendering(still: Fluid?, flowing: Fluid?, textureFluidId: Identifier) {
//        val stillSpriteId =
//            Identifier(textureFluidId.namespace, "block/" + textureFluidId.path.toString() + "_still")
//        val flowingSpriteId =
//            Identifier(textureFluidId.namespace, "block/" + textureFluidId.path.toString() + "_flow")
//
//        // If they're not already present, add the sprites to the block atlas
//        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
//            .register(ClientSpriteRegistryCallback { _, registry: ClientSpriteRegistryCallback.Registry ->
//                registry.register(stillSpriteId)
//                registry.register(flowingSpriteId)
//            })
//        val fluidId: Identifier = Registry.FLUID.getId(still)
//        val listenerId = Identifier(fluidId.namespace, fluidId.path.toString() + "_reload_listener")
//        val fluidSprites = arrayOfNulls<Sprite>(2)
//
//        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
//            .registerReloadListener(object : SimpleSynchronousResourceReloadListener {
//                override fun reload(manager: ResourceManager) {
//                    val atlas = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
//                    fluidSprites[0] = atlas.apply(stillSpriteId)
//                    fluidSprites[1] = atlas.apply(flowingSpriteId)
//                }
//
//                override fun getFabricId(): Identifier = listenerId
//            })
//
//        // The FluidRenderer gets the sprites and color from a FluidRenderHandler during rendering
//        val renderHandler = FluidRenderHandler { _, _, _ -> fluidSprites }
//
//        FluidRenderHandlerRegistry.INSTANCE.register(still, renderHandler)
//        FluidRenderHandlerRegistry.INSTANCE.register(flowing, renderHandler)
//    }
//}