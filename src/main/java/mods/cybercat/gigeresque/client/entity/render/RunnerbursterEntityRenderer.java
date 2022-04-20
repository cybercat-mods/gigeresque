package mods.cybercat.gigeresque.client.entity.render;

import mods.cybercat.gigeresque.client.entity.model.RunnerbursterEntityModel;
import mods.cybercat.gigeresque.client.entity.render.feature.RunnerBusterBloodFeatureRenderer;
import mods.cybercat.gigeresque.common.entity.impl.RunnerbursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class RunnerbursterEntityRenderer extends GeoEntityRenderer<RunnerbursterEntity> {
	public RunnerbursterEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new RunnerbursterEntityModel());
		this.shadowRadius = 0.3f;
		this.addLayer(new RunnerBusterBloodFeatureRenderer(this));
	}

	@Override
	public void render(RunnerbursterEntity entity, float entityYaw, float partialTicks, MatrixStack stack,
			VertexConsumerProvider bufferIn, int packedLightIn) {
		float scaleFactor = 1.0f + (entity.getGrowth() / entity.getMaxGrowth());
		stack.scale(scaleFactor, scaleFactor, scaleFactor);
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	}

	@Override
	protected float getDeathMaxRotation(RunnerbursterEntity entityLivingBaseIn) {
		return 0;
	}

	@Override
	public void renderEarly(RunnerbursterEntity animatable, MatrixStack stackIn, float ticks,
			VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			int packedOverlayIn, float red, float green, float blue, float partialTicks) {
		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
				red, green, blue, partialTicks);
		//stackIn.scale(animatable.age < 10 ? 0 : 1F, animatable.age < 10 ? 0 : 1F, animatable.age < 10 ? 0 : 1F);
	}
}
