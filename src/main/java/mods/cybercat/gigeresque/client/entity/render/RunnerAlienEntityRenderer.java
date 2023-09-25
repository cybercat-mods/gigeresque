package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mod.azuredoom.bettercrawling.Constants;
import mods.cybercat.gigeresque.client.entity.model.RunnerAlienEntityModel;
import mods.cybercat.gigeresque.client.entity.render.feature.RunnerAlienFeatureRenderer;
import mods.cybercat.gigeresque.common.entity.impl.RunnerAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@Environment(EnvType.CLIENT)
public class RunnerAlienEntityRenderer extends GeoEntityRenderer<RunnerAlienEntity> {
	public RunnerAlienEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new RunnerAlienEntityModel());
		this.shadowRadius = 0.5f;
		this.addRenderLayer(new RunnerAlienFeatureRenderer(this));
	}

	@Override
	public void render(RunnerAlienEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
		float scaleFactor = 0.5f + ((entity.getGrowth() / entity.getMaxGrowth()) / 5f);
		stack.scale(scaleFactor, scaleFactor, scaleFactor);
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	}

	@Override
	protected float getDeathMaxRotation(RunnerAlienEntity entityLivingBaseIn) {
		return 0;
	}

	@Override
	public void preRender(PoseStack poseStack, RunnerAlienEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		if (!animatable.isPassenger())
			Constants.onPreRenderLiving(animatable, partialTick, poseStack);
		super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void postRender(PoseStack poseStack, RunnerAlienEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
		if (!animatable.isPassenger())
			Constants.onPostRenderLiving(animatable, partialTick, poseStack, bufferSource);
	}

	@Override
	public float getMotionAnimThreshold(RunnerAlienEntity animatable) {
		return 0.005f;
	}
}
