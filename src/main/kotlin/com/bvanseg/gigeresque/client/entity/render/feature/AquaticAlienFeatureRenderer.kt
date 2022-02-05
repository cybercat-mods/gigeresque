package com.bvanseg.gigeresque.client.entity.render.feature

import com.bvanseg.gigeresque.client.entity.model.EntityModels
import com.bvanseg.gigeresque.client.entity.texture.EntityTextures
import com.bvanseg.gigeresque.client.extensions.blue
import com.bvanseg.gigeresque.client.extensions.green
import com.bvanseg.gigeresque.client.extensions.red
import com.bvanseg.gigeresque.common.entity.impl.AquaticAlienEntity
import com.bvanseg.gigeresque.common.util.clamp
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer
import software.bernie.geckolib3.renderers.geo.IGeoRenderer
import kotlin.math.max

/**
 * @author Boston Vanseghi
 */
class AquaticAlienFeatureRenderer(private val entityRenderer: IGeoRenderer<AquaticAlienEntity>): GeoLayerRenderer<AquaticAlienEntity>(entityRenderer) {

    override fun render(
        matrixStackIn: MatrixStack,
        bufferIn: VertexConsumerProvider,
        packedLightIn: Int,
        aquaticAlienEntity: AquaticAlienEntity,
        limbSwing: Float,
        limbSwingAmount: Float,
        partialTicks: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        val waterColor = aquaticAlienEntity.world.getBiome(aquaticAlienEntity.blockPos).waterColor
        val wr = waterColor.red / 255.0f
        val wg = waterColor.green / 255.0f
        val wb = waterColor.blue / 255.0f

        val r = clamp(wr + (wr / 1.2f), 0.0f, 1.0f)
        val g = clamp(wg + (wg / 1.2f), 0.0f, 1.0f)
        val b = clamp(wb + (wb / 1.2f), 0.0f, 1.0f)

        val uv = if (aquaticAlienEntity.hurtTime > 0) OverlayTexture.field_32953 else OverlayTexture.DEFAULT_UV

        entityRenderer.render(
            entityModel.getModel(EntityModels.AQUATIC_ALIEN),
            aquaticAlienEntity,
            partialTicks,
            RenderLayer.getEntityTranslucent(EntityTextures.AQUATIC_ALIEN_YOUNG),
            matrixStackIn,
            bufferIn,
            bufferIn.getBuffer(RenderLayer.getEntityCutout(EntityTextures.AQUATIC_ALIEN_YOUNG)),
            packedLightIn,
            uv,
            1.0f, 1.0f, 1.0f, ((aquaticAlienEntity.maxGrowth - aquaticAlienEntity.growth) / aquaticAlienEntity.maxGrowth.toFloat())
        )

        entityRenderer.render(
            entityModel.getModel(EntityModels.AQUATIC_ALIEN),
            aquaticAlienEntity,
            partialTicks,
            RenderLayer.getEntityTranslucent(EntityTextures.AQUATIC_ALIEN_TINTABLE),
            matrixStackIn,
            bufferIn,
            bufferIn.getBuffer(RenderLayer.getEntityCutout(EntityTextures.AQUATIC_ALIEN_TINTABLE)),
            packedLightIn,
            uv,
            r, g, b, max((aquaticAlienEntity.growth / aquaticAlienEntity.maxGrowth.toFloat()) - 0.3f, 0f)
        )
    }

    override fun getEntityTexture(entityIn: AquaticAlienEntity) = EntityTextures.AQUATIC_ALIEN_YOUNG
}