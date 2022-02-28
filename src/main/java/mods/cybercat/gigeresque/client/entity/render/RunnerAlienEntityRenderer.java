package mods.cybercat.gigeresque.client.entity.render;

import mods.cybercat.gigeresque.client.entity.model.RunnerAlienEntityModel;
import mods.cybercat.gigeresque.client.entity.render.feature.RunnerAlienFeatureRenderer;
import mods.cybercat.gigeresque.common.entity.impl.RunnerAlienEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class RunnerAlienEntityRenderer extends GeoEntityRenderer<RunnerAlienEntity> {
	public RunnerAlienEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new RunnerAlienEntityModel());
		this.shadowRadius = 0.5f;
		this.addLayer(new RunnerAlienFeatureRenderer(this));
	}

	@Override
	public void render(RunnerAlienEntity entity, float entityYaw, float partialTicks, MatrixStack stack,
			VertexConsumerProvider bufferIn, int packedLightIn) {
		float scaleFactor = 0.5f + ((entity.getGrowth() / entity.getMaxGrowth()) / 5f);
		stack.scale(scaleFactor, scaleFactor, scaleFactor);
		stack.translate(0.0, 0.1, 0.0);
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	}
}
