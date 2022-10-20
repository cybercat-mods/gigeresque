package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mods.cybercat.gigeresque.client.entity.model.RunnerbursterEntityModel;
import mods.cybercat.gigeresque.client.entity.render.feature.RunnerBusterBloodFeatureRenderer;
import mods.cybercat.gigeresque.common.entity.impl.RunnerbursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class RunnerbursterEntityRenderer extends GeoEntityRenderer<RunnerbursterEntity> {
	public RunnerbursterEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new RunnerbursterEntityModel());
		this.shadowRadius = 0.3f;
		this.addLayer(new RunnerBusterBloodFeatureRenderer(this));
	}

	@Override
	public void render(RunnerbursterEntity entity, float entityYaw, float partialTicks, PoseStack stack,
			MultiBufferSource bufferIn, int packedLightIn) {
		float scaleFactor = 1.0f + (entity.getGrowth() / entity.getMaxGrowth());
		stack.scale(scaleFactor, scaleFactor, scaleFactor);
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	}

	@Override
	protected float getDeathMaxRotation(RunnerbursterEntity entityLivingBaseIn) {
		return 0;
	}

	@Override
	public void renderEarly(RunnerbursterEntity animatable, PoseStack stackIn, float ticks,
			MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			int packedOverlayIn, float red, float green, float blue, float partialTicks) {
		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
				red, green, blue, partialTicks);
		//stackIn.scale(animatable.age < 10 ? 0 : 1F, animatable.age < 10 ? 0 : 1F, animatable.age < 10 ? 0 : 1F);
	}
}
