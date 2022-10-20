package mods.cybercat.gigeresque.client.entity.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mods.cybercat.gigeresque.client.entity.model.blocks.JarModel;
import mods.cybercat.gigeresque.common.block.entity.JarStorageEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class JarRender extends GeoBlockRenderer<JarStorageEntity> {
	public JarRender() {
		super(new JarModel());
	}

	@Override
	public RenderType getRenderType(JarStorageEntity animatable, float partialTicks, PoseStack stack,
			MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}

}