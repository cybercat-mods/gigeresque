package com.bvanseg.gigeresque.client.entity.render;

import com.bvanseg.gigeresque.client.entity.model.AquaticAlienEntityModelJava;
import com.bvanseg.gigeresque.client.entity.render.feature.AquaticAlienFeatureRendererJava;
import com.bvanseg.gigeresque.common.entity.impl.AquaticAlienEntityJava;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class AquaticAlienEntityRendererJava extends GeoEntityRenderer<AquaticAlienEntityJava> {
    public AquaticAlienEntityRendererJava(EntityRendererFactory.Context context) {
        super(context, new AquaticAlienEntityModelJava());
        this.shadowRadius = 0.5f;
        this.addLayer(new AquaticAlienFeatureRendererJava(this));
    }

    @Override
    public void render(AquaticAlienEntityJava entity, float entityYaw, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
        float scaleFactor = 0.5f + ((entity.getGrowth() / entity.getMaxGrowth()) / 5f);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }
}
