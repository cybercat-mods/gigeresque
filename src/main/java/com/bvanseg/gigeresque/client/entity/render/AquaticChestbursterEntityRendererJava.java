package com.bvanseg.gigeresque.client.entity.render;

import com.bvanseg.gigeresque.client.entity.model.AquaticChestbursterEntityModelJava;
import com.bvanseg.gigeresque.common.entity.impl.AquaticChestbursterEntityJava;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class AquaticChestbursterEntityRendererJava extends GeoEntityRenderer<AquaticChestbursterEntityJava> {
    public AquaticChestbursterEntityRendererJava(EntityRendererFactory.Context context) {
        super(context, new AquaticChestbursterEntityModelJava());
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(AquaticChestbursterEntityJava entity, float entityYaw, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
        float scaleFactor = 1.0f + (entity.getGrowth() / entity.getMaxGrowth());
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }
}
