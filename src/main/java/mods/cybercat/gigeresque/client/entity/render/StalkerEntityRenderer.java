package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
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
		super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, animatable.walkAnimation.speedOld < 0.35F && !animatable.swinging ? 0.1F : 1.0F);
	}

	@Override
	public void render(StalkerEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
		if (entity.isCrawling()) {
			if (entity.isColliding(entity.blockPosition(), entity.getLevel().getBlockState(entity.blockPosition().west()))) {
				stack.mulPose(Axis.ZP.rotationDegrees(-90));
				stack.translate(0, -0.2, 0);
			}
			if (entity.isColliding(entity.blockPosition(), entity.getLevel().getBlockState(entity.blockPosition()))) {
				stack.mulPose(Axis.ZP.rotationDegrees(-90));
				stack.translate(0, -0.2, 0);
			}
			if (entity.isColliding(entity.blockPosition(), entity.getLevel().getBlockState(entity.blockPosition().north()))) {
				stack.mulPose(Axis.XP.rotationDegrees(90));
				stack.translate(0, -0.2, 0);
			}
			if (entity.isColliding(entity.blockPosition(), entity.getLevel().getBlockState(entity.blockPosition().south()))) {
				stack.mulPose(Axis.XP.rotationDegrees(-90));
				stack.translate(0, -0.2, 0);
			}
			if (entity.isColliding(entity.blockPosition(), entity.getLevel().getBlockState(entity.blockPosition().east()))) {
				stack.mulPose(Axis.ZP.rotationDegrees(90));
				stack.translate(0, -0.2, 0);
			}
//			stack.mulPose(Axis.ZP.rotationDegrees(90));
//			stack.translate(0, 0, 0);
		}
		if (entity.isNoGravity() && !entity.isCrawling() && !entity.isUnderWater()) {
			stack.mulPose(Axis.ZP.rotationDegrees(180));
			stack.translate(0, -0.6, 0);
		}
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	}
}
