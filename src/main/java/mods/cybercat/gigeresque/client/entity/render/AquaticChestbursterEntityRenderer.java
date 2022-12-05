package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;

import mods.cybercat.gigeresque.client.entity.model.AquaticChestbursterEntityModel;
import mods.cybercat.gigeresque.client.entity.render.feature.AquaBusterBloodFeatureRenderer;
import mods.cybercat.gigeresque.common.entity.impl.AquaticChestbursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class AquaticChestbursterEntityRenderer extends GeoEntityRenderer<AquaticChestbursterEntity> {
	public AquaticChestbursterEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new AquaticChestbursterEntityModel());
		this.shadowRadius = 0.5f;
		this.addRenderLayer(new AquaBusterBloodFeatureRenderer(this));
	}

	@Override
	public void render(AquaticChestbursterEntity entity, float entityYaw, float partialTicks, PoseStack stack,
			MultiBufferSource bufferIn, int packedLightIn) {
		float scaleFactor = 1.0f + (entity.getGrowth() / entity.getMaxGrowth());
		stack.scale(scaleFactor, scaleFactor, scaleFactor);
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	}

	@Override
	protected float getDeathMaxRotation(AquaticChestbursterEntity entityLivingBaseIn) {
		return 0;
	}
}
