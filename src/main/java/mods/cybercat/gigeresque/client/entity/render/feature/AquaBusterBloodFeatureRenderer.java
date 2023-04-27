package mods.cybercat.gigeresque.client.entity.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticChestbursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.renderer.layer.GeoRenderLayer;

@Environment(EnvType.CLIENT)
public class AquaBusterBloodFeatureRenderer extends GeoRenderLayer<AquaticChestbursterEntity> {

	public AquaBusterBloodFeatureRenderer(GeoRenderer<AquaticChestbursterEntity> entityRenderer) {
		super(entityRenderer);
	}

	@Override
	public void render(PoseStack poseStack, AquaticChestbursterEntity animatable, BakedGeoModel bakedModel,
			RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick,
			int packedLight, int packedOverlay) {
		var uv = animatable.hurtTime > 0 ? OverlayTexture.NO_WHITE_U : OverlayTexture.NO_OVERLAY;
		var rendertype = RenderType.entityTranslucent(EntityTextures.CHESTBURSTER_BLOOD);
		if (!(animatable.getGrowth() >= 1200))
			renderer.reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, rendertype,
					bufferSource.getBuffer(rendertype), partialTick, packedLight, uv, 1.0f, 1.0f, 1.0f,
					((1200 - animatable.getBlood()) / 1200));
	}
}
