package com.bvanseg.gigeresque.client.entity.render;

import com.bvanseg.gigeresque.client.entity.model.AlienEntityModelJava;
import com.bvanseg.gigeresque.client.entity.render.feature.ClassicAlienFeatureRendererJava;
import com.bvanseg.gigeresque.common.entity.impl.ClassicAlienEntityJava;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class AlienEntityRendererJava extends GeoEntityRenderer<ClassicAlienEntityJava> {
    public AlienEntityRendererJava(EntityRendererFactory.Context context) {
        super(context, new AlienEntityModelJava());
        this.shadowRadius = 0.5f;
        this.addLayer(new ClassicAlienFeatureRendererJava(this));
    }

    @Override
    public void render(ClassicAlienEntityJava entity, float entityYaw, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
        float scaleFactor = 0.5f + ((entity.getGrowth() / entity.getMaxGrowth()) / 5f);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }
}
