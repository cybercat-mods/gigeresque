package mods.cybercat.gigeresque.client.entity.render;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.FacehuggerEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class FacehuggerEntityRenderer extends GeoEntityRenderer<FacehuggerEntity> {
	private final HashMap<EntityType<?>, TransformDataGenerator> headDistances = new HashMap<>();

	public FacehuggerEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new FacehuggerEntityModel());
		this.shadowRadius = 0.2f;
		headDistances.put(EntityType.SHEEP, (facehugger, host) -> new TransformData(0.0, ((double) host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.4, host.getBbWidth() - ((double) facehugger.getBbHeight()) - 0.1, 0.385, calcStandardOffsetY(facehugger) - 0.05));
		headDistances.put(EntityType.COW, (facehugger, host) -> new TransformData(0.0, ((double) host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.4, host.getBbWidth() - ((double) facehugger.getBbHeight()) - 0.1, 0.41, calcStandardOffsetY(facehugger) - 0.05));
		headDistances.put(EntityType.PIG, (facehugger, host) -> new TransformData(0.0, ((double) host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.4, host.getBbWidth() - ((double) facehugger.getBbHeight()) - 0.1, 0.41, // Distance from face
				calcStandardOffsetY(facehugger) - 0.05 // Distance from head
		));
		headDistances.put(EntityType.WOLF, (facehugger, host) -> new TransformData(0.0, ((double) host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.4, host.getBbWidth() - ((double) facehugger.getBbHeight()) - 0.1, 0.54, calcStandardOffsetY(facehugger) - 0.15));
		headDistances.put(EntityType.VILLAGER, (facehugger, host) -> new TransformData(0.0, 0.0, 0.0, 0.36, calcStandardOffsetY(facehugger)));
		headDistances.put(EntityType.DOLPHIN, (facehugger, host) -> new TransformData(0.0, -0.23, 0.0, 0.80, calcStandardOffsetY(facehugger)));
		headDistances.put(EntityType.PLAYER, (facehugger, host) -> new TransformData(0.0, 0.25, 0.0, 0.36, calcStandardOffsetY(facehugger)));
	}

	@Override
	public void render(FacehuggerEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
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

	@Override
	public void preRender(PoseStack poseStack, FacehuggerEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		if (animatable.getEntityData().get(FacehuggerEntity.EGGSPAWN) == true) {
			poseStack.pushPose();
			poseStack.scale(animatable.tickCount < 5 ? 0 : 1F, animatable.tickCount < 5 ? 0 : 1F, animatable.tickCount < 5 ? 0 : 1F);
			poseStack.popPose();
		}
		super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	protected void applyRotations(FacehuggerEntity facehugger, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
		if (facehugger.isAlive() && facehugger.isAttachedToHost()) {
			var host = (LivingEntity) facehugger.getVehicle();
			if (host == null)
				return;
			var transformData = getTransformData(facehugger, host);
			var bodyYaw = Mth.rotLerp(partialTicks, host.yBodyRotO, host.yBodyRot);
			var headYaw = Mth.rotLerp(partialTicks, host.yHeadRotO, host.yHeadRot) - bodyYaw;
			var headPitch = Mth.rotLerp(partialTicks, host.getXRot(), host.xRotO);

			// translate head-center
			matrixStackIn.mulPose(Axis.YN.rotationDegrees(bodyYaw));
			matrixStackIn.translate(transformData.originX, transformData.originY, transformData.originZ);
			// yaw
			matrixStackIn.mulPose(Axis.YN.rotationDegrees(headYaw));
			// pitch
			matrixStackIn.mulPose(Axis.XP.rotationDegrees(headPitch));
			matrixStackIn.translate(0.0, transformData.headOffset, transformData.faceOffset); // apply offsets

		} else
			super.applyRotations(facehugger, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
	}

	private TransformData getTransformData(FacehuggerEntity facehugger, Entity host) {
		return headDistances.computeIfAbsent(host.getType(), entityType -> (facehugger1, host1) -> new TransformData(0.0, 0.0, 0.0, ((double) host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + host.getBbWidth(), calcStandardOffsetY(facehugger))).invoke(facehugger, host);
	}

	private double calcStandardOffsetY(FacehuggerEntity facehugger) {
		return -facehugger.getBbWidth();
	}

	private class TransformData {
		double originX;
		double originY;
		double originZ;
		double faceOffset;
		double headOffset;

		public TransformData(double originX, double originY, double originZ, double faceOffset, double headOffset) {
			this.originX = originX;
			this.originY = originY;
			this.originZ = originZ;
			this.faceOffset = faceOffset;
			this.headOffset = headOffset;
		}
	}

	private interface TransformDataGenerator {
		public TransformData invoke(FacehuggerEntity facehugger, Entity host);
	}

	@Override
	protected float getDeathMaxRotation(FacehuggerEntity entityLivingBaseIn) {
		return 0;
	}
}
