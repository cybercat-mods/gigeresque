package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.StalkerEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
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
	public void render(StalkerEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
		if (entity.isCrawling()) {
			if (entity.isColliding(entity.blockPosition(), entity.level().getBlockState(entity.blockPosition().west()))) {
				stack.mulPose(Axis.ZP.rotationDegrees(-90));
				stack.translate(0, -0.2, 0);
			}
			if (entity.isColliding(entity.blockPosition(), entity.level().getBlockState(entity.blockPosition()))) {
				stack.mulPose(Axis.ZP.rotationDegrees(-90));
				stack.translate(0, -0.2, 0);
			}
			if (entity.isColliding(entity.blockPosition(), entity.level().getBlockState(entity.blockPosition().north()))) {
				stack.mulPose(Axis.XP.rotationDegrees(90));
				stack.translate(0, -0.2, 0);
			}
			if (entity.isColliding(entity.blockPosition(), entity.level().getBlockState(entity.blockPosition().south()))) {
				stack.mulPose(Axis.XP.rotationDegrees(-90));
				stack.translate(0, -0.2, 0);
			}
			if (entity.isColliding(entity.blockPosition(), entity.level().getBlockState(entity.blockPosition().east()))) {
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
