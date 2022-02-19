package com.bvanseg.gigeresque.client.entity.render;

import com.bvanseg.gigeresque.client.entity.model.RunnerbursterEntityModelJava;
import com.bvanseg.gigeresque.common.entity.impl.RunnerbursterEntityJava;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class RunnerbursterEntityRendererJava extends GeoEntityRenderer<RunnerbursterEntityJava> {
    public RunnerbursterEntityRendererJava(EntityRendererFactory.Context context) {
        super(context, new RunnerbursterEntityModelJava());
        this.shadowRadius = 0.3f;
    }

    @Override
    public void render(RunnerbursterEntityJava entity, float entityYaw, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
        float scaleFactor = 1.0f + (entity.getGrowth() / entity.getMaxGrowth());
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }
}
