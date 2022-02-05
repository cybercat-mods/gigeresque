package com.bvanseg.gigeresque.client.entity.render

import com.bvanseg.gigeresque.client.entity.model.ChestbursterEntityModel
import com.bvanseg.gigeresque.client.extensions.scale
import com.bvanseg.gigeresque.common.entity.impl.ChestbursterEntity
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer

/**
 * @author Boston Vanseghi
 */
class ChestbursterEntityRenderer(context: EntityRendererFactory.Context) : GeoEntityRenderer<ChestbursterEntity>(
    context,
    ChestbursterEntityModel()
) {
    init {
        this.shadowRadius = 0.1f
    }

    override fun render(
        entity: ChestbursterEntity,
        entityYaw: Float,
        partialTicks: Float,
        stack: MatrixStack,
        bufferIn: VertexConsumerProvider,
        packedLightIn: Int
    ) {
        stack.scale(1.0f + ((entity.growth / entity.maxGrowth.toFloat()) / 4.0f))
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn)
    }
}