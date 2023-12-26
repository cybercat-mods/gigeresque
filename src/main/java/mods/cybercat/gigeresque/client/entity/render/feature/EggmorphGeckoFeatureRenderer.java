package mods.cybercat.gigeresque.client.entity.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
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
        var progress = 0.0F + (((Eggmorphable) animatable).getTicksUntilEggmorphed() / 6000);
        var renderLayer = EggmorphFeatureRenderer.getEggmorphLayerTexture(getGeoModel().getTextureResource(animatable)).renderLayer;
        if (((Eggmorphable) animatable).isEggmorphing())
            renderer.reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, renderLayer, bufferSource.getBuffer(renderLayer), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, progress);
    }
}
