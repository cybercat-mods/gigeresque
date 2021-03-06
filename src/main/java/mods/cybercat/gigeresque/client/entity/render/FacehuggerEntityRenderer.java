package mods.cybercat.gigeresque.client.entity.render;

import java.util.HashMap;

import mods.cybercat.gigeresque.client.entity.model.FacehuggerEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.FacehuggerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class FacehuggerEntityRenderer extends GeoEntityRenderer<FacehuggerEntity> {
	private final HashMap<EntityType<?>, TransformDataGenerator> headDistances = new HashMap<>();

	public FacehuggerEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new FacehuggerEntityModel());
		this.shadowRadius = 0.2f;
		headDistances.put(EntityType.SHEEP, (facehugger, host) -> new TransformData(0.0,
				((double) host.getEyeHeight(host.getPose())) - host.getMountedHeightOffset() - facehugger.getWidth()
						+ 0.4,
				host.getWidth() - ((double) facehugger.getHeight()) - 0.1, 0.385,
				calcStandardOffsetY(facehugger) - 0.05));
		headDistances.put(EntityType.COW, (facehugger, host) -> new TransformData(0.0,
				((double) host.getEyeHeight(host.getPose())) - host.getMountedHeightOffset() - facehugger.getWidth()
						+ 0.4,
				host.getWidth() - ((double) facehugger.getHeight()) - 0.1, 0.41,
				calcStandardOffsetY(facehugger) - 0.05));
		headDistances.put(EntityType.PIG, (facehugger, host) -> new TransformData(0.0,
				((double) host.getEyeHeight(host.getPose())) - host.getMountedHeightOffset() - facehugger.getWidth()
						+ 0.4,
				host.getWidth() - ((double) facehugger.getHeight()) - 0.1, 0.41, // Distance from face
				calcStandardOffsetY(facehugger) - 0.05 // Distance from head
		));
		headDistances.put(EntityType.WOLF, (facehugger, host) -> new TransformData(0.0,
				((double) host.getEyeHeight(host.getPose())) - host.getMountedHeightOffset() - facehugger.getWidth()
						+ 0.4,
				host.getWidth() - ((double) facehugger.getHeight()) - 0.1, 0.54,
				calcStandardOffsetY(facehugger) - 0.15));
		headDistances.put(EntityType.VILLAGER,
				(facehugger, host) -> new TransformData(0.0, 0.0, 0.0, 0.36, calcStandardOffsetY(facehugger)));
		headDistances.put(EntityType.DOLPHIN,
				(facehugger, host) -> new TransformData(0.0, -0.23, 0.0, 0.80, calcStandardOffsetY(facehugger)));
		headDistances.put(EntityType.PLAYER,
				(facehugger, host) -> new TransformData(0.0, 0.25, 0.0, 0.36, calcStandardOffsetY(facehugger)));
	}

	@Override
	public void render(FacehuggerEntity entity, float entityYaw, float partialTicks, MatrixStack stack,
			VertexConsumerProvider bufferIn, int packedLightIn) {
		if (entity.isCrawling() && !entity.hasNoGravity()) {
			if (entity.collidesWithStateAtPos(entity.getBlockPos(),
					entity.world.getBlockState(entity.getBlockPos().west()))) {
				stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-90));
				stack.translate(0, -0.2, 0);
			}
			if (entity.collidesWithStateAtPos(entity.getBlockPos(), entity.world.getBlockState(entity.getBlockPos()))) {
				stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-90));
				stack.translate(0, -0.2, 0);
			}
			if (entity.collidesWithStateAtPos(entity.getBlockPos(),
					entity.world.getBlockState(entity.getBlockPos().north()))) {
				stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
				stack.translate(0, -0.2, 0);
			}
			if (entity.collidesWithStateAtPos(entity.getBlockPos(),
					entity.world.getBlockState(entity.getBlockPos().south()))) {
				stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90));
				stack.translate(0, -0.2, 0);
			}
			if (entity.collidesWithStateAtPos(entity.getBlockPos(),
					entity.world.getBlockState(entity.getBlockPos().east()))) {
				stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90));
				stack.translate(0, -0.2, 0);
			}
		}
		if (entity.hasNoGravity() && !entity.isCrawling() && !entity.isSubmergedInWater()) {
			stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180));
			stack.translate(0, -0.6, 0);
		}
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	}

	@Override
	public void renderEarly(FacehuggerEntity animatable, MatrixStack stackIn, float ticks,
			VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			int packedOverlayIn, float red, float green, float blue, float partialTicks) {
		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
				red, green, blue, partialTicks);
		if (animatable.getDataTracker().get(FacehuggerEntity.EGGSPAWN) == true)
		stackIn.scale(animatable.age < 5 ? 0 : 1F, animatable.age < 5 ? 0 : 1F, animatable.age < 5 ? 0 : 1F);
	}

	@Override
	protected void applyRotations(FacehuggerEntity facehugger, MatrixStack matrixStackIn, float ageInTicks,
			float rotationYaw, float partialTicks) {
		if (facehugger.isAlive() && facehugger.isAttachedToHost()) {
			var host = (LivingEntity) facehugger.getVehicle();
			if (host == null)
				return;
			var transformData = getTransformData(facehugger, host);
			var bodyYaw = MathHelper.lerpAngleDegrees(partialTicks, host.prevBodyYaw, host.bodyYaw);
			var headYaw = MathHelper.lerpAngleDegrees(partialTicks, host.prevHeadYaw, host.headYaw) - bodyYaw;
			var headPitch = MathHelper.lerpAngleDegrees(partialTicks, host.getPitch(), host.prevPitch);

			// translate head-center
			matrixStackIn.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(bodyYaw));
			matrixStackIn.translate(transformData.originX, transformData.originY, transformData.originZ);
			// yaw
			matrixStackIn.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(headYaw));
			// pitch
			matrixStackIn.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(headPitch));
			matrixStackIn.translate(0.0, transformData.headOffset, transformData.faceOffset); // apply offsets

		} else {
			super.applyRotations(facehugger, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
		}
	}

	private TransformData getTransformData(FacehuggerEntity facehugger, Entity host) {
		return headDistances
				.computeIfAbsent(host.getType(),
						entityType -> (facehugger1, host1) -> new TransformData(0.0, 0.0, 0.0,
								((double) host.getEyeHeight(host.getPose())) - host.getMountedHeightOffset()
										- facehugger.getWidth() + host.getWidth(),
								calcStandardOffsetY(facehugger)))
				.invoke(facehugger, host);
	}

	private double calcStandardOffsetY(FacehuggerEntity facehugger) {
		return -facehugger.getWidth();
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
