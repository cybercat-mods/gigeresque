package com.bvanseg.gigeresque.client.entity.render;

import com.bvanseg.gigeresque.common.entity.impl.ChestbursterEntityJava;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class ChestbursterEntityRendererJava extends GeoEntityRenderer<ChestbursterEntityJava> {
    public ChestbursterEntityRendererJava(EntityRendererFactory.Context ctx) {
        super(ctx, new ChestbursterEntityModelJava());
        this.shadowRadius = 0.1f;
    }

    @Override
    public void render(ChestbursterEntityJava entity, float entityYaw, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
        float scaleFactor = 1.0f + ((entity.getGrowth() / entity.getMaxGrowth()) / 4.0f);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }
}
