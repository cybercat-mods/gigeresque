package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mod.azure.azurelib.common.internal.common.cache.object.BakedGeoModel;
import mods.cybercat.gigeresque.client.entity.model.RunnerbursterEntityModel;
import mods.cybercat.gigeresque.client.entity.render.feature.RunnerBusterBloodFeatureRenderer;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerbursterEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RunnerbursterEntityRenderer extends GeoEntityRenderer<RunnerbursterEntity> {
    public RunnerbursterEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new RunnerbursterEntityModel());
        this.shadowRadius = 0.3f;
        this.addRenderLayer(new RunnerBusterBloodFeatureRenderer(this));
    }

    @Override
    public void preRender(PoseStack poseStack, RunnerbursterEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
        float scaleFactor = 1.0f + (animatable.getGrowth() / animatable.getMaxGrowth());
        poseStack.pushPose();
        poseStack.scale(scaleFactor, scaleFactor, scaleFactor);
        poseStack.popPose();
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight,
                packedOverlay, color);
    }

    @Override
    protected float getDeathMaxRotation(RunnerbursterEntity entityLivingBaseIn) {
        return 0;
    }
}
