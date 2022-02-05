package com.bvanseg.gigeresque.client.entity.render.feature

import com.bvanseg.gigeresque.Constants
import com.bvanseg.gigeresque.client.entity.texture.EggmorphLayerTexture
import com.bvanseg.gigeresque.interfacing.Eggmorphable
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.Identifier

/**
 * @author Aelpecyem
 */
@Environment(EnvType.CLIENT)
class EggmorphFeatureRenderer<T : Entity, M : EntityModel<T>>(context: FeatureRendererContext<T, M>) :
    FeatureRenderer<T, M>(context) {

    companion object {
        private val textureCache = HashMap<Identifier, EggmorphLayerTexture>()

        @JvmStatic
        fun <T : Entity> renderEggmorphedModel(
            renderedModel: EntityModel<T>,
            texture: Identifier,
            matrices: MatrixStack,
            vertexConsumers: VertexConsumerProvider,
            light: Int,
            entity: T,
            limbAngle: Float,
            limbDistance: Float,
            tickDelta: Float,
            animationProgress: Float,
            headYaw: Float,
            headPitch: Float
        ) {
            matrices.push()
            renderedModel.animateModel(entity, limbAngle, limbDistance, tickDelta)
            val vertexConsumer = vertexConsumers.getBuffer(
                getEggmorphLayerTexture(
                    texture
                ).renderLayer
            )
            val progress = 1 - (entity as Eggmorphable).ticksUntilEggmorphed / (Constants.EGGMORPH_DURATION.toFloat())
            renderedModel.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch)
            renderedModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1F, 1F, 1F, progress)
            matrices.pop()
        }

        @JvmStatic
        fun getEggmorphLayerTexture(texture: Identifier): EggmorphLayerTexture {
            return textureCache.computeIfAbsent(
                texture
            ) {
                EggmorphLayerTexture(
                    MinecraftClient.getInstance().textureManager,
                    MinecraftClient.getInstance().resourceManager,
                    texture
                )
            }
        }
    }

    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        entity: T,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        if (entity is Eggmorphable && entity.isEggmorphing) {
            renderEggmorphedModel(
                contextModel,
                getTexture(entity),
                matrices,
                vertexConsumers,
                light,
                entity,
                limbAngle,
                limbDistance,
                tickDelta,
                animationProgress,
                headYaw,
                headPitch
            )
        }
    }
}