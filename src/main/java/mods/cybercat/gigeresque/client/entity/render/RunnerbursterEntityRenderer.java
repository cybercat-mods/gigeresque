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
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class RunnerbursterEntityRenderer extends GeoEntityRenderer<RunnerbursterEntity> {
	public RunnerbursterEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new RunnerbursterEntityModel());
		this.shadowRadius = 0.3f;
		this.addRenderLayer(new RunnerBusterBloodFeatureRenderer(this));
	}

	@Override
	public void preRender(PoseStack poseStack, RunnerbursterEntity animatable, BakedGeoModel model,
			MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick,
			int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		float scaleFactor = 1.0f + (animatable.getGrowth() / animatable.getMaxGrowth());
		poseStack.pushPose();
		poseStack.scale(scaleFactor, scaleFactor, scaleFactor);
		poseStack.popPose();
		super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight,
				packedOverlay, red, green, blue, alpha);
	}

	@Override
	protected float getDeathMaxRotation(RunnerbursterEntity entityLivingBaseIn) {
		return 0;
	}
}
