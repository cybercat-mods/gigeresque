package com.bvanseg.gigeresque.client.entity.render;

import com.bvanseg.gigeresque.client.entity.model.RunnerAlienEntityModelJava;
import com.bvanseg.gigeresque.client.entity.render.feature.RunnerAlienFeatureRendererJava;
import com.bvanseg.gigeresque.common.entity.impl.RunnerAlienEntityJava;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class RunnerAlienEntityRendererJava extends GeoEntityRenderer<RunnerAlienEntityJava> {
    public RunnerAlienEntityRendererJava(EntityRendererFactory.Context context) {
        super(context, new RunnerAlienEntityModelJava());
        this.shadowRadius = 0.5f;
        this.addLayer(new RunnerAlienFeatureRendererJava(this));
    }

    @Override
    public void render(RunnerAlienEntityJava entity, float entityYaw, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
        float scaleFactor = 0.5f + ((entity.getGrowth() / entity.getMaxGrowth()) / 5f);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        stack.translate(0.0, 0.1, 0.0);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }
}
