package mods.cybercat.gigeresque.client.entity.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.RunnerAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
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
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn,
			RunnerAlienEntity runnerAlienEntity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch) {
		var uv = runnerAlienEntity.hurtTime > 0 ? OverlayTexture.NO_WHITE_U : OverlayTexture.NO_OVERLAY;
		if (!(runnerAlienEntity.getGrowth() >= runnerAlienEntity.getMaxGrowth()))
			entityRenderer.render(getEntityModel().getModel(EntityModels.RUNNER_ALIEN), runnerAlienEntity, partialTicks,
					RenderType.entityTranslucent(EntityTextures.RUNNER_ALIEN_YOUNG), matrixStackIn, bufferIn,
					bufferIn.getBuffer(RenderType.entityTranslucent(EntityTextures.RUNNER_ALIEN_YOUNG)), packedLightIn,
					uv, 1.0f, 1.0f, 1.0f, ((runnerAlienEntity.getMaxGrowth() - runnerAlienEntity.getGrowth())
							/ runnerAlienEntity.getMaxGrowth()));
	}

	@Override
	protected ResourceLocation getEntityTexture(RunnerAlienEntity entityIn) {
		return EntityTextures.RUNNER_ALIEN_YOUNG;
	}
}
