package mods.cybercat.gigeresque.client.entity.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.common.api.client.renderer.layer.GeoRenderLayer;
import mod.azure.azurelib.common.internal.client.renderer.GeoRenderer;
import mod.azure.azurelib.common.internal.common.cache.object.BakedGeoModel;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;

@Environment(EnvType.CLIENT)
public class BusterBloodFeatureRenderer extends GeoRenderLayer<ChestbursterEntity> {

    public BusterBloodFeatureRenderer(GeoRenderer<ChestbursterEntity> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, ChestbursterEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        var uv = animatable.hurtTime > 0 ? OverlayTexture.NO_WHITE_U : OverlayTexture.NO_OVERLAY;
        var rendertype = RenderType.entityTranslucent(EntityTextures.CHESTBURSTER_BLOOD);
        if (animatable.getGrowth() < 1200)
            renderer.reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, rendertype, bufferSource.getBuffer(rendertype), partialTick, packedLight, uv, 1.0f, 1.0f, 1.0f, ((1200 - animatable.getBlood()) / 1200));
    }
}
