package mods.cybercat.gigeresque.client.entity.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.ClassicAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@Environment(EnvType.CLIENT)
public class ClassicAlienFeatureRenderer extends GeoLayerRenderer<ClassicAlienEntity> {
	private IGeoRenderer<ClassicAlienEntity> entityRenderer;

	public ClassicAlienFeatureRenderer(IGeoRenderer<ClassicAlienEntity> entityRenderer) {
		super(entityRenderer);
		this.entityRenderer = entityRenderer;
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn,
			ClassicAlienEntity alienEntity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch) {
		var uv = alienEntity.hurtTime > 0 ? OverlayTexture.NO_WHITE_U : OverlayTexture.NO_OVERLAY;
		if (!(alienEntity.getGrowth() >= alienEntity.getMaxGrowth()))
			entityRenderer.render(getEntityModel().getModel(EntityModels.ALIEN), alienEntity, partialTicks,
					RenderType.entityTranslucent(EntityTextures.ALIEN_YOUNG), matrixStackIn, bufferIn,
					bufferIn.getBuffer(RenderType.entityTranslucent(EntityTextures.ALIEN_YOUNG)), packedLightIn, uv,
					1.0f, 1.0f, 1.0f, ((1200 - alienEntity.getGrowth()) / 1200));
	}

	@Override
	protected ResourceLocation getEntityTexture(ClassicAlienEntity entityIn) {
		return EntityTextures.ALIEN_YOUNG;
	}
}
