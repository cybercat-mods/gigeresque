package com.bvanseg.gigeresque.client.entity.render.feature;

import com.bvanseg.gigeresque.Constants;
import com.bvanseg.gigeresque.interfacing.Eggmorphable;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class EggmorphGeoFeatureRenderer<T extends LivingEntity & IAnimatable> extends GeoLayerRenderer<T> {
	private IGeoRenderer<T> entityRenderer;

	public EggmorphGeoFeatureRenderer(IGeoRenderer<T> entityRenderer) {
		super(entityRenderer);
		this.entityRenderer = entityRenderer;
	}

	@SuppressWarnings("resource")
	public static <T extends Entity, M extends GeoModel> void renderEggmorphedModel(IGeoRenderer<T> entityRenderer,
			M renderedModel, Identifier texture, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
			int light, T entity, float tickDelta) {
		var progress = 1 - ((Eggmorphable) entity).getTicksUntilEggmorphed() / ((float) Constants.EGGMORPH_DURATION);
		var renderLayer = EggmorphFeatureRenderer.getEggmorphLayerTexture(texture).renderLayer;
		entityRenderer.render(renderedModel, entity, tickDelta, renderLayer, matrices, vertexConsumers,
				vertexConsumers.getBuffer(renderLayer), light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, progress);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity,
			float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw,
			float headPitch) {
		if (entity instanceof Eggmorphable && ((Eggmorphable) entity).isEggmorphing()) {
			renderEggmorphedModel(entityRenderer,
					entityRenderer.getGeoModelProvider()
							.getModel(entityRenderer.getGeoModelProvider().getModelLocation(entity)),
					getEntityTexture(entity), matrices, vertexConsumers, light, entity, tickDelta);
		}
	}
}
