package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;

import mods.cybercat.gigeresque.client.entity.model.AquaticAlienEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.AquaticAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class AquaticAlienEntityRenderer extends GeoEntityRenderer<AquaticAlienEntity> {
	public AquaticAlienEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new AquaticAlienEntityModel());
		this.shadowRadius = 0.5f;
	}

	@Override
	public void render(AquaticAlienEntity entity, float entityYaw, float partialTicks, PoseStack stack,
			MultiBufferSource bufferIn, int packedLightIn) {
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	}

	@Override
	protected float getDeathMaxRotation(AquaticAlienEntity entityLivingBaseIn) {
		return 0;
	}
}
