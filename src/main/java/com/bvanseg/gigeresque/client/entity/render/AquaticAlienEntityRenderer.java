package com.bvanseg.gigeresque.client.entity.render;

import com.bvanseg.gigeresque.client.entity.model.AquaticAlienEntityModel;
import com.bvanseg.gigeresque.client.entity.render.feature.AquaticAlienFeatureRenderer;
import com.bvanseg.gigeresque.common.entity.impl.AquaticAlienEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class AquaticAlienEntityRenderer extends GeoEntityRenderer<AquaticAlienEntity> {
	public AquaticAlienEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new AquaticAlienEntityModel());
		this.shadowRadius = 0.5f;
		this.addLayer(new AquaticAlienFeatureRenderer(this));
	}

	@Override
	public void render(AquaticAlienEntity entity, float entityYaw, float partialTicks, MatrixStack stack,
			VertexConsumerProvider bufferIn, int packedLightIn) {
		float scaleFactor = 0.5f + ((entity.getGrowth() / entity.getMaxGrowth()) / 5f);
		stack.scale(scaleFactor, scaleFactor, scaleFactor);
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	}
}
