package mods.cybercat.gigeresque.client.entity.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mods.cybercat.gigeresque.client.entity.model.blocks.SarcophagusModel;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class SarcophagusRender extends GeoBlockRenderer<AlienStorageEntity> {
	public SarcophagusRender() {
		super(new SarcophagusModel());
	}

	@Override
	public RenderType getRenderType(AlienStorageEntity animatable, float partialTicks, PoseStack stack,
			MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}

}