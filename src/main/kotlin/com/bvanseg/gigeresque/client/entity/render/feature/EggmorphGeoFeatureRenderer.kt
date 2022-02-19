//package com.bvanseg.gigeresque.client.entity.render.feature
//
//import com.bvanseg.gigeresque.Constants
//import com.bvanseg.gigeresque.interfacing.Eggmorphable
//import net.fabricmc.api.EnvType
//import net.fabricmc.api.Environment
//import net.minecraft.client.render.OverlayTexture
//import net.minecraft.client.render.VertexConsumerProvider
//import net.minecraft.client.util.math.MatrixStack
//import net.minecraft.entity.Entity
//import net.minecraft.util.Identifier
//import software.bernie.geckolib3.core.IAnimatable
//import software.bernie.geckolib3.geo.render.built.GeoModel
//import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer
//import software.bernie.geckolib3.renderers.geo.IGeoRenderer
//
///**
// * @author Aelpecyem
// */
//@Environment(EnvType.CLIENT)
//class EggmorphGeoFeatureRenderer<T>(private val entityRenderer: IGeoRenderer<T>) :
//    GeoLayerRenderer<T>(entityRenderer) where T : Entity, T : IAnimatable? {
//
//    companion object {
//        @JvmStatic
//        fun <T : Entity, M : GeoModel> renderEggmorphedModel(
//            entityRenderer: IGeoRenderer<T>,
//            renderedModel: M,
//            texture: Identifier,
//            matrices: MatrixStack,
//            vertexConsumers: VertexConsumerProvider,
//            light: Int,
//            entity: T,
//            tickDelta: Float,
//        ) {
//            val progress = 1 - (entity as Eggmorphable).ticksUntilEggmorphed / (Constants.EGGMORPH_DURATION.toFloat())
//            val renderLayer = EggmorphFeatureRenderer.getEggmorphLayerTexture(texture).renderLayer
//            entityRenderer.render(
//                renderedModel,
//                entity,
//                tickDelta,
//                renderLayer,
//                matrices,
//                vertexConsumers,
//                vertexConsumers.getBuffer(renderLayer),
//                light,
//                OverlayTexture.DEFAULT_UV,
//                1.0f, 1.0f, 1.0f, progress
//            )
//        }
//    }
//
//    override fun render(
//        matrices: MatrixStack,
//        vertexConsumers: VertexConsumerProvider,
//        light: Int,
//        entity: T,
//        limbAngle: Float,
//        limbDistance: Float,
//        tickDelta: Float,
//        animationProgress: Float,
//        headYaw: Float,
//        headPitch: Float
//    ) {
//        if (entity is Eggmorphable && entity.isEggmorphing) {
//            renderEggmorphedModel(
//                entityRenderer,
//                entityRenderer.geoModelProvider.getModel(entityRenderer.geoModelProvider.getModelLocation(entity)),
//                getEntityTexture(entity),
//                matrices,
//                vertexConsumers,
//                light,
//                entity,
//                tickDelta
//            )
//        }
//    }
//}