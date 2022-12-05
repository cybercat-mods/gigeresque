package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import mods.cybercat.gigeresque.client.entity.model.AlienEntityModel;
import mods.cybercat.gigeresque.client.entity.render.feature.ClassicAlienFeatureRenderer;
import mods.cybercat.gigeresque.common.entity.impl.ClassicAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class AlienEntityRenderer extends GeoEntityRenderer<ClassicAlienEntity> {

	public AlienEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new AlienEntityModel());
		this.shadowRadius = 0.5f;
		this.addRenderLayer(new ClassicAlienFeatureRenderer(this));
	}

	@Override
	public void render(ClassicAlienEntity entity, float entityYaw, float partialTicks, PoseStack stack,
			MultiBufferSource bufferIn, int packedLightIn) {
		float scaleFactor = 0.8f + ((entity.getGrowth() / entity.getMaxGrowth()) / 5f);
		stack.scale(scaleFactor, scaleFactor, scaleFactor);
		if (entity.isCrawling()) {
			if (entity.isColliding(entity.blockPosition(), entity.level.getBlockState(entity.blockPosition().west()))) {
				stack.mulPose(Axis.ZP.rotationDegrees(-90));
				stack.translate(0, -0.5, 0);
			}
			if (entity.isColliding(entity.blockPosition(),
					entity.level.getBlockState(entity.blockPosition().north()))) {
				stack.mulPose(Axis.XP.rotationDegrees(90));
				stack.translate(0, -0.5, 0);
			}
			if (entity.isColliding(entity.blockPosition(),
					entity.level.getBlockState(entity.blockPosition().south()))) {
				stack.mulPose(Axis.XP.rotationDegrees(-90));
				stack.translate(0, -0.5, 0);
			}
			if (entity.isColliding(entity.blockPosition(), entity.level.getBlockState(entity.blockPosition().east()))) {
				stack.mulPose(Axis.ZP.rotationDegrees(90));
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
