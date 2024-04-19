package mods.cybercat.gigeresque.client.entity.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class EggmorphGeckoFeatureRenderer<T extends Entity & GeoEntity> extends GeoRenderLayer<T> {

    public EggmorphGeckoFeatureRenderer(GeoRenderer<T> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        var progress = 0.0F + (Gigeresque.config.getEggmorphTickTimer() / (Gigeresque.config.getEggmorphTickTimer() / 2));
        var renderLayer = EggmorphFeatureRenderer.getEggmorphLayerTexture(
                getGeoModel().getTextureResource(animatable)).renderLayer;
        if (animatable instanceof LivingEntity livingEntity && livingEntity.hasEffect(GigStatusEffects.EGGMORPHING))
            renderer.reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, renderLayer,
                    bufferSource.getBuffer(renderLayer), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1.0f,
                    1.0f, 1.0f, progress);
    }
}
