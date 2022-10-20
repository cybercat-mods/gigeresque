package mods.cybercat.gigeresque.client.entity.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.AquaticChestbursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@Environment(EnvType.CLIENT)
public class AquaBusterBloodFeatureRenderer extends GeoLayerRenderer<AquaticChestbursterEntity> {
	private IGeoRenderer<AquaticChestbursterEntity> entityRenderer;

	public AquaBusterBloodFeatureRenderer(IGeoRenderer<AquaticChestbursterEntity> entityRenderer) {
		super(entityRenderer);
		this.entityRenderer = entityRenderer;
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn,
			AquaticChestbursterEntity alienEntity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch) {
		var uv = alienEntity.hurtTime > 0 ? OverlayTexture.NO_WHITE_U : OverlayTexture.NO_OVERLAY;
		if (!(alienEntity.getBlood() >= 1200))
			entityRenderer.render(getEntityModel().getModel(EntityModels.AQUATIC_CHESTBURSTER), alienEntity,
					partialTicks, RenderType.entityTranslucent(EntityTextures.CHESTBURSTER_BLOOD), matrixStackIn,
					bufferIn, bufferIn.getBuffer(RenderType.entityTranslucent(EntityTextures.CHESTBURSTER_BLOOD)),
					packedLightIn, uv, 1.0f, 1.0f, 1.0f, ((1200 - alienEntity.getBlood()) / 1200));
	}

	@Override
	protected ResourceLocation getEntityTexture(AquaticChestbursterEntity entityIn) {
		return EntityTextures.CHESTBURSTER_BLOOD;
	}
}
