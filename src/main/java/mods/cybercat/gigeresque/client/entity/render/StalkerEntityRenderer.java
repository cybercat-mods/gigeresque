package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mod.azuredoom.bettercrawling.Constants;
import mods.cybercat.gigeresque.client.entity.model.StalkerEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@Environment(EnvType.CLIENT)
public class StalkerEntityRenderer extends GeoEntityRenderer<StalkerEntity> {
	public StalkerEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new StalkerEntityModel());
		this.shadowRadius = 1.0f;
	}

	@Override
	protected float getDeathMaxRotation(StalkerEntity entityLivingBaseIn) {
		return 0.0F;
	}

	@Override
	public void actuallyRender(PoseStack poseStack, StalkerEntity animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, animatable.walkAnimation.speedOld < 0.45F && !animatable.swinging ? 0.11F : 1.0F);
	}

	@Override
	public void preRender(PoseStack poseStack, StalkerEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		if (!animatable.isPassenger())
			Constants.onPreRenderLiving(animatable, partialTick, poseStack);
		super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void postRender(PoseStack poseStack, StalkerEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
		if (!animatable.isPassenger())
			Constants.onPostRenderLiving(animatable, partialTick, poseStack, bufferSource);
	}
	
	@Override
	public float getMotionAnimThreshold(StalkerEntity animatable) {
		return 0.005f;
	}
}
