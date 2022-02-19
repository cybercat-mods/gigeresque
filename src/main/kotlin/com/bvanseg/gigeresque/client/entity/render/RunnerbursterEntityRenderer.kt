//package com.bvanseg.gigeresque.client.entity.render
//
//import com.bvanseg.gigeresque.client.entity.model.RunnerbursterEntityModel
//import com.bvanseg.gigeresque.client.extensions.scale
//import com.bvanseg.gigeresque.common.entity.impl.RunnerbursterEntity
//import net.fabricmc.api.EnvType
//import net.fabricmc.api.Environment
//import net.minecraft.client.render.VertexConsumerProvider
//import net.minecraft.client.render.entity.EntityRendererFactory
//import net.minecraft.client.util.math.MatrixStack
//import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer
//
///**
// * @author Boston Vanseghi
// */
//@Environment(EnvType.CLIENT)
//class RunnerbursterEntityRenderer(context: EntityRendererFactory.Context) : GeoEntityRenderer<RunnerbursterEntity>(
//    context,
//    RunnerbursterEntityModel()
//) {
//    init {
//        this.shadowRadius = 0.3f
//    }
//
//    override fun render(
//        entity: RunnerbursterEntity,
//        entityYaw: Float,
//        partialTicks: Float,
//        stack: MatrixStack,
//        bufferIn: VertexConsumerProvider,
//        packedLightIn: Int
//    ) {
//        stack.scale(1.0f + (entity.growth / entity.maxGrowth.toFloat()))
//        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn)
//    }
//}