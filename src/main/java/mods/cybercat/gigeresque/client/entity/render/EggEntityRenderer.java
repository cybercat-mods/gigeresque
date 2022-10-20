package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mods.cybercat.gigeresque.client.entity.model.EggEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.AlienEggEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class EggEntityRenderer extends GeoEntityRenderer<AlienEggEntity> {
	public EggEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new EggEntityModel());
		this.shadowRadius = 0.5f;
	}

	@Override
	public RenderType getRenderType(AlienEggEntity animatable, float partialTicks, PoseStack stack,
			MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(textureLocation);
	}

	@Override
	protected float getDeathMaxRotation(AlienEggEntity entityLivingBaseIn) {
		return 0.0F;
	}
}
