package mods.cybercat.gigeresque.client.entity.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;

import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class EggmorphGeckoFeatureRenderer<T extends LivingEntity & IAnimatable> extends GeoLayerRenderer<T> {

	private IGeoRenderer<T> entityRenderer;

	public EggmorphGeckoFeatureRenderer(IGeoRenderer<T> entityRenderer) {
		super(entityRenderer);
		this.entityRenderer = entityRenderer;
	}

	public static <T extends Entity, M extends GeoModel> void renderEggmorphedModel(IGeoRenderer<T> entityRenderer,
			M renderedModel, ResourceLocation texture, PoseStack matrices, MultiBufferSource vertexConsumers, int light,
			T entity, float tickDelta) {
		var progress = 0.0F + (((Eggmorphable) entity).getTicksUntilEggmorphed() / 6000);
		var renderLayer = EggmorphFeatureRenderer.getEggmorphLayerTexture(texture).renderLayer;
		entityRenderer.render(renderedModel, entity, tickDelta, renderLayer, matrices, vertexConsumers,
				vertexConsumers.getBuffer(renderLayer), light, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, progress);
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, T entity, float limbAngle,
			float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (entity instanceof Eggmorphable && ((Eggmorphable) entity).isEggmorphing()) {
			renderEggmorphedModel(entityRenderer,
					entityRenderer.getGeoModelProvider()
							.getModel(entityRenderer.getGeoModelProvider().getModelResource(entity)),
					getEntityTexture(entity), matrices, vertexConsumers, light, entity, tickDelta);
		}
	}
}
