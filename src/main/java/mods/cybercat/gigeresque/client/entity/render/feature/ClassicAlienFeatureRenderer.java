package mods.cybercat.gigeresque.client.entity.render.feature;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.ClassicAlienEntity;
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
public class ClassicAlienFeatureRenderer extends GeoLayerRenderer<ClassicAlienEntity> {
	private IGeoRenderer<ClassicAlienEntity> entityRenderer;

	public ClassicAlienFeatureRenderer(IGeoRenderer<ClassicAlienEntity> entityRenderer) {
		super(entityRenderer);
		this.entityRenderer = entityRenderer;
	}

	@Override
	public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn,
			ClassicAlienEntity alienEntity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch) {
		var uv = alienEntity.hurtTime > 0 ? OverlayTexture.field_32953 : OverlayTexture.DEFAULT_UV;
		if (!(alienEntity.getGrowth() >= alienEntity.getMaxGrowth()))
			entityRenderer.render(getEntityModel().getModel(EntityModels.ALIEN), alienEntity, partialTicks,
					RenderLayer.getEntityTranslucent(EntityTextures.ALIEN_YOUNG), matrixStackIn, bufferIn,
					bufferIn.getBuffer(RenderLayer.getEntityTranslucent(EntityTextures.ALIEN_YOUNG)), packedLightIn, uv,
					1.0f, 1.0f, 1.0f, ((1200 - alienEntity.getGrowth()) / 1200));
	}

	@Override
	protected Identifier getEntityTexture(ClassicAlienEntity entityIn) {
		return EntityTextures.ALIEN_YOUNG;
	}
}
