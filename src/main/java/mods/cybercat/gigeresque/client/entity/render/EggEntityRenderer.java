package mods.cybercat.gigeresque.client.entity.render;

import mods.cybercat.gigeresque.client.entity.model.EggEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.AlienEggEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class EggEntityRenderer extends GeoEntityRenderer<AlienEggEntity> {
	public EggEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new EggEntityModel());
		this.shadowRadius = 0.5f;
	}

	@Override
	public RenderLayer getRenderType(AlienEggEntity animatable, float partialTicks, MatrixStack stack,
			VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			Identifier textureLocation) {
		return RenderLayer.getEntityTranslucent(textureLocation);
	}

	@Override
	protected float getDeathMaxRotation(AlienEggEntity entityLivingBaseIn) {
		return 0.0F;
	}
}
