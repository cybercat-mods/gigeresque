package mods.cybercat.gigeresque.client.entity.render;

import mods.cybercat.gigeresque.client.entity.model.ChestbursterEntityModel;
import mods.cybercat.gigeresque.client.entity.render.feature.BusterBloodFeatureRenderer;
import mods.cybercat.gigeresque.common.entity.impl.ChestbursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class ChestbursterEntityRenderer extends GeoEntityRenderer<ChestbursterEntity> {
	public ChestbursterEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx, new ChestbursterEntityModel());
		this.shadowRadius = 0.1f;
		this.addLayer(new BusterBloodFeatureRenderer(this));
	}

	@Override
	public void render(ChestbursterEntity entity, float entityYaw, float partialTicks, MatrixStack stack,
			VertexConsumerProvider bufferIn, int packedLightIn) {
		float scaleFactor = 1.0f + ((entity.getGrowth() / entity.getMaxGrowth()) / 4.0f);
		stack.scale(scaleFactor, scaleFactor, scaleFactor);
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	}

	@Override
	protected float getDeathMaxRotation(ChestbursterEntity entityLivingBaseIn) {
		return 0;
	}
}
