package mods.cybercat.gigeresque.client.entity.render.feature;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.AquaticChestbursterEntity;
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
public class AquaBusterBloodFeatureRenderer extends GeoLayerRenderer<AquaticChestbursterEntity> {
	private IGeoRenderer<AquaticChestbursterEntity> entityRenderer;

	public AquaBusterBloodFeatureRenderer(IGeoRenderer<AquaticChestbursterEntity> entityRenderer) {
		super(entityRenderer);
		this.entityRenderer = entityRenderer;
	}

	@Override
	public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn,
			AquaticChestbursterEntity alienEntity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch) {
		var uv = alienEntity.hurtTime > 0 ? OverlayTexture.field_32953 : OverlayTexture.DEFAULT_UV;
		entityRenderer.render(getEntityModel().getModel(EntityModels.AQUATIC_CHESTBURSTER), alienEntity, partialTicks,
				RenderLayer.getEntityTranslucent(EntityTextures.CHESTBURSTER_BLOOD), matrixStackIn, bufferIn,
				bufferIn.getBuffer(RenderLayer.getEntityCutout(EntityTextures.CHESTBURSTER_BLOOD)), packedLightIn, uv, 1.0f,
				1.0f, 1.0f, (alienEntity.age < 302 ? (alienEntity.age + 9600) % 300 / 2 : 0));
	}

	@Override
	protected Identifier getEntityTexture(AquaticChestbursterEntity entityIn) {
		return EntityTextures.CHESTBURSTER_BLOOD;
	}
}
