//package com.bvanseg.gigeresque.client.entity.texture
//
//import com.bvanseg.gigeresque.common.Gigeresque
//import net.fabricmc.api.EnvType
//import net.fabricmc.api.Environment
//import net.minecraft.client.MinecraftClient
//import net.minecraft.client.render.RenderLayer
//import net.minecraft.client.texture.NativeImage
//import net.minecraft.client.texture.NativeImageBackedTexture
//import net.minecraft.client.texture.TextureManager
//import net.minecraft.resource.ResourceManager
//import net.minecraft.util.Identifier
//
///**
// * @author Aelpecyem
// */
//@Environment(EnvType.CLIENT)
//class EggmorphLayerTexture(textureManager: TextureManager, resourceManager: ResourceManager, base: Identifier) :
//    AutoCloseable {
//
//    companion object {
//        private val layerTexture = Identifier(
//            Gigeresque.MOD_ID,
//            "textures/misc/eggmorph_overlay.png"
//        )
//    }
//
//    val renderLayer: RenderLayer
//    private var texture: NativeImageBackedTexture
//
//    override fun close() {
//        texture.close()
//    }
//
//    init {
//        texture = NativeImageBackedTexture(NativeImage(128, 128, true))
//        MinecraftClient.getInstance().execute {
//            try {
//                val baseImage = NativeImage.read(resourceManager.getResource(base).inputStream)
//                val layerImage = NativeImage.read(
//                    resourceManager.getResource(layerTexture).inputStream
//                )
//                texture = NativeImageBackedTexture(NativeImage(layerImage.width, layerImage.height, true))
//                val height = layerImage.height
//                val width = layerImage.width
//                val heightRatio = height / baseImage.height.toFloat()
//                val widthRatio = width / baseImage.width.toFloat()
//                for (y in 0 until height) {
//                    for (x in 0 until width) {
//                        val color = baseImage.getColor((x / widthRatio).toInt(), (y / heightRatio).toInt())
//                        val alpha = color shr 24 and 255
//                        if (alpha > 25) {
//                            texture.image?.setColor(x, y, layerImage.getColor(x, y))
//                        }
//                    }
//                }
//                texture.upload()
//                baseImage.close()
//            } catch (e: Exception) {
//                Gigeresque.LOGGER.error("Could not generate eggmorph texture for $base.", e)
//            }
//        }
//        val id = textureManager.registerDynamicTexture("eggmorph_layer/" + base.path, texture)
//        renderLayer = RenderLayer.getEntityTranslucent(id)
//    }
//}
