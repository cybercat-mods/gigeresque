package mods.cybercat.gigeresque.client.entity.render;

import mods.cybercat.gigeresque.client.entity.model.AlienEntityModel;
import mods.cybercat.gigeresque.client.entity.render.feature.ClassicAlienFeatureRenderer;
import mods.cybercat.gigeresque.common.entity.impl.ClassicAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class AlienEntityRenderer extends GeoEntityRenderer<ClassicAlienEntity> {
	public AlienEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new AlienEntityModel());
		this.shadowRadius = 0.5f;
		this.addLayer(new ClassicAlienFeatureRenderer(this));
	}

	@Override
	public void render(ClassicAlienEntity entity, float entityYaw, float partialTicks, MatrixStack stack,
			VertexConsumerProvider bufferIn, int packedLightIn) {
		float scaleFactor = 0.8f + ((entity.getGrowth() / entity.getMaxGrowth()) / 5f);
		stack.scale(scaleFactor, scaleFactor, scaleFactor);
//		if (entity.isCrawling()) {
//			if (entity.getHorizontalFacing() == Direction.WEST)
//				stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-90));
//			if (entity.getHorizontalFacing() == Direction.NORTH)
//				stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
//			if (entity.getHorizontalFacing() == Direction.SOUTH)
//				stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90));
//			if (entity.getHorizontalFacing() == Direction.EAST)
//				stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90));
//		}
		if (entity.isCrawling()) {
			if (entity.collidesWithStateAtPos(entity.getBlockPos(),
					entity.world.getBlockState(entity.getBlockPos().west()))) {
				stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-90));
				stack.translate(0, -0.5, 0);
			}
			if (entity.collidesWithStateAtPos(entity.getBlockPos(),
					entity.world.getBlockState(entity.getBlockPos().north()))) {
				stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
				stack.translate(0, -0.5, 0);
			}
			if (entity.collidesWithStateAtPos(entity.getBlockPos(),
					entity.world.getBlockState(entity.getBlockPos().south()))) {
				stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90));
				stack.translate(0, -0.5, 0);
			}
			if (entity.collidesWithStateAtPos(entity.getBlockPos(),
					entity.world.getBlockState(entity.getBlockPos().east()))) {
				stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90));
				stack.translate(0, -0.5, 0);
			}
		}
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	}

	@Override
	protected float getDeathMaxRotation(ClassicAlienEntity entityLivingBaseIn) {
		return 0;
	}
}
