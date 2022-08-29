package mods.cybercat.gigeresque.client.entity.render.feature;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.RunnerbursterEntity;
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
public class RunnerBusterBloodFeatureRenderer extends GeoLayerRenderer<RunnerbursterEntity> {
	private IGeoRenderer<RunnerbursterEntity> entityRenderer;

	public RunnerBusterBloodFeatureRenderer(IGeoRenderer<RunnerbursterEntity> entityRenderer) {
		super(entityRenderer);
		this.entityRenderer = entityRenderer;
	}

	@Override
	public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn,
			RunnerbursterEntity alienEntity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch) {
		var uv = alienEntity.hurtTime > 0 ? OverlayTexture.field_32953 : OverlayTexture.DEFAULT_UV;
		if (!(alienEntity.getBlood() >= 1200) && alienEntity.isBirthed() == true)
			entityRenderer.render(getEntityModel().getModel(EntityModels.RUNNERBURSTER), alienEntity, partialTicks,
					RenderLayer.getEntityTranslucent(EntityTextures.CHESTBURSTER_BLOOD), matrixStackIn, bufferIn,
					bufferIn.getBuffer(RenderLayer.getEntityTranslucent(EntityTextures.CHESTBURSTER_BLOOD)), packedLightIn,
					uv, 1.0f, 1.0f, 1.0f, ((1200 - alienEntity.getBlood()) / 1200));
	}

	@Override
	protected Identifier getEntityTexture(RunnerbursterEntity entityIn) {
		return EntityTextures.CHESTBURSTER_BLOOD;
	}
}
