package mods.cybercat.gigeresque.client.entity.render.feature;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.RunnerAlienEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@Environment(EnvType.CLIENT)
public class RunnerAlienFeatureRenderer extends GeoLayerRenderer<RunnerAlienEntity> {
	IGeoRenderer<RunnerAlienEntity> entityRenderer;

	public RunnerAlienFeatureRenderer(IGeoRenderer<RunnerAlienEntity> entityRenderer) {
		super(entityRenderer);
		this.entityRenderer = entityRenderer;
	}

	@Override
	public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn,
			RunnerAlienEntity runnerAlienEntity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch) {
		var uv = runnerAlienEntity.hurtTime > 0 ? OverlayTexture.field_32953 : OverlayTexture.DEFAULT_UV;

		entityRenderer.render(getEntityModel().getModel(EntityModels.RUNNER_ALIEN), runnerAlienEntity, partialTicks,
				RenderLayer.getEntityTranslucent(EntityTextures.RUNNER_ALIEN_YOUNG), matrixStackIn, bufferIn,
				bufferIn.getBuffer(RenderLayer.getEntityCutout(EntityTextures.RUNNER_ALIEN_YOUNG)), packedLightIn, uv,
				1.0f, 1.0f, 1.0f, ((runnerAlienEntity.getMaxGrowth() - runnerAlienEntity.getGrowth())
						/ runnerAlienEntity.getMaxGrowth()));
	}

	@Override
	protected Identifier getEntityTexture(RunnerAlienEntity entityIn) {
		return EntityTextures.RUNNER_ALIEN_YOUNG;
	}
}
