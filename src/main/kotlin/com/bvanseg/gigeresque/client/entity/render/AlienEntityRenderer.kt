package com.bvanseg.gigeresque.client.entity.render

import com.bvanseg.gigeresque.client.entity.model.AlienEntityModel
import com.bvanseg.gigeresque.client.entity.render.feature.ClassicAlienFeatureRenderer
import com.bvanseg.gigeresque.client.extensions.scale
import com.bvanseg.gigeresque.common.entity.impl.ClassicAlienEntity
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer

/**
 * @author Boston Vanseghi
 */
class AlienEntityRenderer(context: EntityRendererFactory.Context) : GeoEntityRenderer<ClassicAlienEntity>(
    context,
    AlienEntityModel()
) {
    init {
        this.shadowRadius = 0.5f
        this.addLayer(ClassicAlienFeatureRenderer(this))
    }

    override fun render(
        entity: ClassicAlienEntity,
        entityYaw: Float,
        partialTicks: Float,
        stack: MatrixStack,
        bufferIn: VertexConsumerProvider,
        packedLightIn: Int
    ) {
        stack.scale(0.5f + ((entity.growth / entity.maxGrowth.toFloat()) / 5f))
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn)
    }
}