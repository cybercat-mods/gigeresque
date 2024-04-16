package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mod.azure.azurelib.common.internal.common.cache.object.BakedGeoModel;
import mods.cybercat.gigeresque.client.entity.model.SpitterModel;
import mods.cybercat.gigeresque.common.entity.impl.extra.SpitterEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SpitterRenderer extends GeoEntityRenderer<SpitterEntity> {
    public SpitterRenderer(EntityRendererProvider.Context context) {
        super(context, new SpitterModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public void preRender(PoseStack poseStack, SpitterEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight,
                packedOverlay, red, green, blue, alpha);
        poseStack.scale(0.6F, 0.6F, 0.6F);
    }

    @Override
    protected float getDeathMaxRotation(SpitterEntity entityLivingBaseIn) {
        return 0.0F;
    }

    @Override
    public float getMotionAnimThreshold(SpitterEntity animatable) {
        return 0.005f;
    }
}
