package mods.cybercat.gigeresque.client.entity.render.blocks;

import mods.cybercat.gigeresque.client.entity.model.blocks.SarcophagusModel;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class SarcophagusRender extends GeoBlockRenderer<AlienStorageEntity> {
	public SarcophagusRender() {
		super(new SarcophagusModel());
	}

	@Override
	public RenderLayer getRenderType(AlienStorageEntity animatable, float partialTicks, MatrixStack stack,
			VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			Identifier textureLocation) {
		return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
	}

}