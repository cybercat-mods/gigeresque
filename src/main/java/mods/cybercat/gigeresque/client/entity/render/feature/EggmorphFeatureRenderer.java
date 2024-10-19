package mods.cybercat.gigeresque.client.entity.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import mods.cybercat.gigeresque.client.entity.texture.EggmorphLayerTexture;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;

public class EggmorphFeatureRenderer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private static int fovEggticker = 0;

    private static final HashMap<ResourceLocation, EggmorphLayerTexture> textureCache = new HashMap<>();

    public EggmorphFeatureRenderer(RenderLayerParent<T, M> context) {
        super(context);
    }

    public static <T extends Entity> void renderEggmorphedModel(
        EntityModel<T> renderedModel,
        ResourceLocation texture,
        PoseStack matrices,
        MultiBufferSource vertexConsumers,
        int light,
        T entity,
        float limbAngle,
        float limbDistance,
        float tickDelta,
        float animationProgress,
        float headYaw,
        float headPitch
    ) {
        matrices.pushPose();
        renderedModel.prepareMobModel(entity, limbAngle, limbDistance, tickDelta);
        var vertexConsumer = vertexConsumers.getBuffer(getEggmorphLayerTexture(texture).renderLayer);
        fovEggticker++;
        var progress = Math.max(0, Math.min(fovEggticker / Gigeresque.config.getEggmorphTickTimer(), 1));
        renderedModel.setupAnim(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        renderedModel.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, progress);
        matrices.popPose();
    }

    public static EggmorphLayerTexture getEggmorphLayerTexture(ResourceLocation texture) {
        return textureCache.computeIfAbsent(
            texture,
            identifier -> new EggmorphLayerTexture(
                Minecraft.getInstance().getTextureManager(),
                Minecraft.getInstance().getResourceManager(),
                texture
            )
        );
    }

    @Override
    public void render(
        @NotNull PoseStack matrices,
        @NotNull MultiBufferSource vertexConsumers,
        int light,
        @NotNull T entity,
        float limbAngle,
        float limbDistance,
        float tickDelta,
        float animationProgress,
        float headYaw,
        float headPitch
    ) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(GigStatusEffects.EGGMORPHING)) {
            renderEggmorphedModel(
                getParentModel(),
                getTextureLocation(entity),
                matrices,
                vertexConsumers,
                light,
                entity,
                limbAngle,
                limbDistance,
                tickDelta,
                animationProgress,
                headYaw,
                headPitch
            );
        }
    }
}
