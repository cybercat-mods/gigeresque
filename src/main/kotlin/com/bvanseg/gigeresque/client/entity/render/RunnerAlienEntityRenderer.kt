package com.bvanseg.gigeresque.client.entity.render

import com.bvanseg.gigeresque.client.entity.model.RunnerAlienEntityModel
import com.bvanseg.gigeresque.client.entity.render.feature.RunnerAlienFeatureRenderer
import com.bvanseg.gigeresque.client.extensions.scale
import com.bvanseg.gigeresque.common.entity.impl.RunnerAlienEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer

/**
 * @author Boston Vanseghi
 */
@Environment(EnvType.CLIENT)
class RunnerAlienEntityRenderer(context: EntityRendererFactory.Context) : GeoEntityRenderer<RunnerAlienEntity>(
    context,
    RunnerAlienEntityModel()
) {
    init {
        this.shadowRadius = 0.5f
        this.addLayer(RunnerAlienFeatureRenderer(this))
    }

    override fun render(
        entity: RunnerAlienEntity,
        entityYaw: Float,
        partialTicks: Float,
        stack: MatrixStack,
        bufferIn: VertexConsumerProvider,
        packedLightIn: Int
    ) {
        stack.scale(0.5f + ((entity.growth / entity.maxGrowth.toFloat()) / 5f))
        stack.translate(0.0, 0.1, 0.0)
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn)
    }
}