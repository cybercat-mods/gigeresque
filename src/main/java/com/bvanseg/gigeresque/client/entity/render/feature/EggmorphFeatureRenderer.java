package com.bvanseg.gigeresque.client.entity.render.feature;

import java.util.HashMap;

import com.bvanseg.gigeresque.Constants;
import com.bvanseg.gigeresque.client.entity.texture.EggmorphLayerTexture;
import com.bvanseg.gigeresque.interfacing.Eggmorphable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class EggmorphFeatureRenderer<T extends Entity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
	public EggmorphFeatureRenderer(FeatureRendererContext<T, M> context) {
		super(context);
	}

	private static final HashMap<Identifier, EggmorphLayerTexture> textureCache = new HashMap<Identifier, EggmorphLayerTexture>();

	@SuppressWarnings("resource")
	public static <T extends Entity> void renderEggmorphedModel(EntityModel<T> renderedModel, Identifier texture,
			MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle,
			float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		matrices.push();
		renderedModel.animateModel(entity, limbAngle, limbDistance, tickDelta);
		var vertexConsumer = vertexConsumers.getBuffer(getEggmorphLayerTexture(texture).renderLayer);
		var progress = 1 - ((Eggmorphable) entity).getTicksUntilEggmorphed() / ((float) Constants.EGGMORPH_DURATION);
		renderedModel.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		renderedModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1F, 1F, 1F, progress);
		matrices.pop();
	}

	public static EggmorphLayerTexture getEggmorphLayerTexture(Identifier texture) {
		return textureCache.computeIfAbsent(texture,
				identifier -> new EggmorphLayerTexture(MinecraftClient.getInstance().getTextureManager(),
						MinecraftClient.getInstance().getResourceManager(), texture));
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity,
			float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw,
			float headPitch) {
		if (entity instanceof Eggmorphable && ((Eggmorphable) entity).isEggmorphing()) {
			renderEggmorphedModel(getContextModel(), getTexture(entity), matrices, vertexConsumers, light, entity,
					limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
		}
	}
}
