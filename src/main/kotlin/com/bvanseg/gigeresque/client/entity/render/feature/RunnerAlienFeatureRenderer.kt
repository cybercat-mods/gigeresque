package com.bvanseg.gigeresque.client.entity.render.feature

import com.bvanseg.gigeresque.client.entity.model.EntityModels
import com.bvanseg.gigeresque.client.entity.texture.EntityTextures
import com.bvanseg.gigeresque.common.entity.impl.RunnerAlienEntity
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
class RunnerAlienFeatureRenderer(private val entityRenderer: IGeoRenderer<RunnerAlienEntity>) :
    GeoLayerRenderer<RunnerAlienEntity>(entityRenderer) {

    override fun render(
        matrixStackIn: MatrixStack,
        bufferIn: VertexConsumerProvider,
        packedLightIn: Int,
        runnerAlienEntity: RunnerAlienEntity,
        limbSwing: Float,
        limbSwingAmount: Float,
        partialTicks: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        val uv = if (runnerAlienEntity.hurtTime > 0) OverlayTexture.field_32953 else OverlayTexture.DEFAULT_UV

        entityRenderer.render(
            entityModel.getModel(EntityModels.RUNNER_ALIEN),
            runnerAlienEntity,
            partialTicks,
            RenderLayer.getEntityTranslucent(EntityTextures.RUNNER_ALIEN_YOUNG),
            matrixStackIn,
            bufferIn,
            bufferIn.getBuffer(RenderLayer.getEntityCutout(EntityTextures.RUNNER_ALIEN_YOUNG)),
            packedLightIn,
            uv,
            1.0f,
            1.0f,
            1.0f,
            ((runnerAlienEntity.maxGrowth - runnerAlienEntity.growth) / runnerAlienEntity.maxGrowth.toFloat())
        )
    }

    override fun getEntityTexture(entityIn: RunnerAlienEntity) = EntityTextures.RUNNER_ALIEN_YOUNG
}