package mods.cybercat.gigeresque.client.entity.render;

import mods.cybercat.gigeresque.client.entity.model.AquaticChestbursterEntityModel;
import mods.cybercat.gigeresque.client.entity.render.feature.AquaBusterBloodFeatureRenderer;
import mods.cybercat.gigeresque.common.entity.impl.AquaticChestbursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class AquaticChestbursterEntityRenderer extends GeoEntityRenderer<AquaticChestbursterEntity> {
	public AquaticChestbursterEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new AquaticChestbursterEntityModel());
		this.shadowRadius = 0.5f;
		this.addLayer(new AquaBusterBloodFeatureRenderer(this));
	}

	@Override
	public void render(AquaticChestbursterEntity entity, float entityYaw, float partialTicks, MatrixStack stack,
			VertexConsumerProvider bufferIn, int packedLightIn) {
		float scaleFactor = 1.0f + (entity.getGrowth() / entity.getMaxGrowth());
		stack.scale(scaleFactor, scaleFactor, scaleFactor);
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	}

	@Override
	protected float getDeathMaxRotation(AquaticChestbursterEntity entityLivingBaseIn) {
		return 0;
	}
}
