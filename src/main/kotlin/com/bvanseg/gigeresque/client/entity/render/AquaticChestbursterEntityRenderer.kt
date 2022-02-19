//package com.bvanseg.gigeresque.client.entity.render
//
//import com.bvanseg.gigeresque.client.entity.model.AquaticChestbursterEntityModel
//import com.bvanseg.gigeresque.client.extensions.scale
//import com.bvanseg.gigeresque.common.entity.impl.AquaticChestbursterEntity
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
//class AquaticChestbursterEntityRenderer(context: EntityRendererFactory.Context) :
//    GeoEntityRenderer<AquaticChestbursterEntity>(
//        context,
//        AquaticChestbursterEntityModel()
//    ) {
//    init {
//        this.shadowRadius = 0.1f
//    }
//
//    override fun render(
//        entity: AquaticChestbursterEntity,
//        entityYaw: Float,
//        partialTicks: Float,
//        stack: MatrixStack,
//        bufferIn: VertexConsumerProvider,
//        packedLightIn: Int
//    ) {
//        stack.scale(1.0f + (entity.growth / entity.maxGrowth))
//        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn)
//    }
//}