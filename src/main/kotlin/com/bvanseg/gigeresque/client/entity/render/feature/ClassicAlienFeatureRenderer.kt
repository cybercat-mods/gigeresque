package com.bvanseg.gigeresque.client.entity.render.feature

import com.bvanseg.gigeresque.client.entity.model.EntityModels
import com.bvanseg.gigeresque.client.entity.texture.EntityTextures
import com.bvanseg.gigeresque.common.entity.impl.ClassicAlienEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer
import software.bernie.geckolib3.renderers.geo.IGeoRenderer

/**
 * @author Boston Vanseghi
 */
@Environment(EnvType.CLIENT)
class ClassicAlienFeatureRenderer(private val entityRenderer: IGeoRenderer<ClassicAlienEntity>) :
    GeoLayerRenderer<ClassicAlienEntity>(entityRenderer) {

    override fun render(
        matrixStackIn: MatrixStack,
        bufferIn: VertexConsumerProvider,
        packedLightIn: Int,
        alienEntity: ClassicAlienEntity,
        limbSwing: Float,
        limbSwingAmount: Float,
        partialTicks: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        val uv = if (alienEntity.hurtTime > 0) OverlayTexture.field_32953 else OverlayTexture.DEFAULT_UV

        entityRenderer.render(
            entityModel.getModel(EntityModels.ALIEN),
            alienEntity,
            partialTicks,
            RenderLayer.getEntityTranslucent(EntityTextures.ALIEN_YOUNG),
            matrixStackIn,
            bufferIn,
            bufferIn.getBuffer(RenderLayer.getEntityCutout(EntityTextures.ALIEN_YOUNG)),
            packedLightIn,
            uv,
            1.0f, 1.0f, 1.0f, ((alienEntity.maxGrowth - alienEntity.growth) / alienEntity.maxGrowth.toFloat())
        )
    }

    override fun getEntityTexture(entityIn: ClassicAlienEntity) = EntityTextures.ALIEN_YOUNG
}