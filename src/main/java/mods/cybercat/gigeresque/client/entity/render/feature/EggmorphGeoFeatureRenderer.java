package mods.cybercat.gigeresque.client.entity.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.renderer.layer.GeoRenderLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;

public class EggmorphGeoFeatureRenderer<T extends Entity & GeoEntity> extends GeoRenderLayer<T> {

    private int fovEggticker = 0;

    public EggmorphGeoFeatureRenderer(GeoRenderer<T> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(
        PoseStack poseStack,
        T animatable,
        BakedGeoModel bakedModel,
        RenderType renderType,
        MultiBufferSource bufferSource,
        VertexConsumer buffer,
        float partialTick,
        int packedLight,
        int packedOverlay
    ) {
        var renderLayer = EggmorphFeatureRenderer.getEggmorphLayerTexture(
            getGeoModel().getTextureResource(animatable)
        ).renderLayer;
        if (animatable instanceof LivingEntity livingEntity && livingEntity.hasEffect(GigStatusEffects.EGGMORPHING)) {
            fovEggticker++;
            var progress = Math.max(0, Math.min(fovEggticker / Gigeresque.config.getEggmorphTickTimer(), 1));
            renderer.reRender(
                getDefaultBakedModel(animatable),
                poseStack,
                bufferSource,
                animatable,
                renderLayer,
                bufferSource.getBuffer(renderLayer),
                partialTick,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                1.0f,
                1.0f,
                1.0f,
                progress
            );
        }
    }
}
