package mods.cybercat.gigeresque.client.entity.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.ChestbursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@Environment(EnvType.CLIENT)
public class BusterBloodFeatureRenderer extends GeoRenderLayer<ChestbursterEntity> {

	public BusterBloodFeatureRenderer(GeoRenderer<ChestbursterEntity> entityRenderer) {
		super(entityRenderer);
	}

	@Override
	public void render(PoseStack poseStack, ChestbursterEntity animatable, BakedGeoModel bakedModel,
			RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick,
			int packedLight, int packedOverlay) {
		var uv = animatable.hurtTime > 0 ? OverlayTexture.NO_WHITE_U : OverlayTexture.NO_OVERLAY;
		if (!(animatable.getGrowth() >= 1200))
			renderer.reRender(getGeoModel().getBakedModel(EntityModels.CHESTBURSTER), poseStack, bufferSource,
					animatable, RenderType.entityTranslucent(EntityTextures.CHESTBURSTER_BLOOD),
					bufferSource.getBuffer(RenderType.entityTranslucent(EntityTextures.CHESTBURSTER_BLOOD)),
					partialTick, packedLight, uv, 1.0f, 1.0f, 1.0f, ((1200 - animatable.getBlood()) / 1200));
	}
}
