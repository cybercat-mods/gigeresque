package mods.cybercat.gigeresque.client.entity.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.renderer.layer.GeoRenderLayer;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.RunnerAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;

@Environment(EnvType.CLIENT)
public class RunnerAlienFeatureRenderer extends GeoRenderLayer<RunnerAlienEntity> {

    public RunnerAlienFeatureRenderer(GeoRenderer<RunnerAlienEntity> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, RunnerAlienEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        var uv = animatable.hurtTime > 0 ? OverlayTexture.NO_WHITE_U : OverlayTexture.NO_OVERLAY;
        var rendertype = RenderType.entityTranslucent(EntityTextures.RUNNER_ALIEN_YOUNG);
        if (animatable.getGrowth() < animatable.getMaxGrowth())
            renderer.reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, rendertype, bufferSource.getBuffer(rendertype), partialTick, packedLight, uv, 1.0f, 1.0f, 1.0f, ((1200 - animatable.getGrowth()) / 1200));
    }
}
