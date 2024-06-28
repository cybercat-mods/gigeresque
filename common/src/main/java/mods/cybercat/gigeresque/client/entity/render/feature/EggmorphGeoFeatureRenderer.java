package mods.cybercat.gigeresque.client.entity.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.common.api.client.renderer.layer.GeoRenderLayer;
import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mod.azure.azurelib.common.internal.client.renderer.GeoRenderer;
import mod.azure.azurelib.common.internal.common.cache.object.BakedGeoModel;
import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EggmorphGeoFeatureRenderer<T extends Entity & GeoEntity> extends GeoRenderLayer<T> {

    private int fovEggticker = 0;

    public EggmorphGeoFeatureRenderer(GeoRenderer<T> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        var renderLayer = EggmorphFeatureRenderer.getEggmorphLayerTexture(
                getGeoModel().getTextureResource(animatable)).renderLayer;
        if (animatable instanceof LivingEntity livingEntity && livingEntity.hasEffect(GigStatusEffects.EGGMORPHING)) {
            fovEggticker++;
            var progress = Math.max(0, Math.min(fovEggticker / CommonMod.config.getEggmorphTickTimer(), 1));
            var vertexConsumer = bufferSource.getBuffer(renderLayer);
            vertexConsumer.setColor(1, 1,1, progress);
            renderer.reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, renderLayer,
                    vertexConsumer, partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1.0f,
                    1.0f, 1.0f, progress);
        }
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
    }
}
