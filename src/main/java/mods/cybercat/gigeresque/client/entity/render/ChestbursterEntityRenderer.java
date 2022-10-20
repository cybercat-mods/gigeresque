package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;

import mods.cybercat.gigeresque.client.entity.model.ChestbursterEntityModel;
import mods.cybercat.gigeresque.client.entity.render.feature.BusterBloodFeatureRenderer;
import mods.cybercat.gigeresque.common.entity.impl.ChestbursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class ChestbursterEntityRenderer extends GeoEntityRenderer<ChestbursterEntity> {
	public ChestbursterEntityRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new ChestbursterEntityModel());
		this.shadowRadius = 0.1f;
		this.addLayer(new BusterBloodFeatureRenderer(this));
	}

	@Override
	public void render(ChestbursterEntity entity, float entityYaw, float partialTicks, PoseStack stack,
			MultiBufferSource bufferIn, int packedLightIn) {
		float scaleFactor = 1.0f + ((entity.getGrowth() / entity.getMaxGrowth()) / 4.0f);
		stack.scale(scaleFactor, scaleFactor, scaleFactor);
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	}

	@Override
	protected float getDeathMaxRotation(ChestbursterEntity entityLivingBaseIn) {
		return 0;
	}
}
