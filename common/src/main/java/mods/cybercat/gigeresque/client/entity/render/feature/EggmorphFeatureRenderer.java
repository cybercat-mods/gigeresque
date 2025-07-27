package mods.cybercat.gigeresque.client.entity.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.core.object.Color;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.client.entity.texture.EggmorphLayerTexture;

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
        var progress = Math.clamp(((CommonMod.config.getEggmorphTickTimer() / 2  - fovEggticker) / (CommonMod.config.getEggmorphTickTimer() / 2)), -1, 1);
        var alpha = (int) (progress * 0xFF) << 24;
        vertexConsumer.setColor(Color.ofOpaque(alpha).argbInt());
        renderedModel.setupAnim(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        renderedModel.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, -1);
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
        if (entity instanceof LivingEntity livingEntity && livingEntity.getInBlockState().is(GigTags.NEST_CROSS_BLOCKS)) {
            if (livingEntity.tickCount % 20 == 0)
                fovEggticker++;
            if (fovEggticker > CommonMod.config.getEggmorphTickTimer()) {
                fovEggticker = 0;
            }
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
        } else {
            fovEggticker = 0;
        }
    }
}
