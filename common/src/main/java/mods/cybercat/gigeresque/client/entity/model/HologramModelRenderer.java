package mods.cybercat.gigeresque.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.AzLayerRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityModelRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererPipeline;
import mods.cybercat.gigeresque.common.entity.impl.misc.HologramEntity;

public class HologramModelRenderer extends AzEntityModelRenderer<HologramEntity> {

    public HologramModelRenderer(
            AzEntityRendererPipeline<HologramEntity> entityRendererPipeline,
            AzLayerRenderer<HologramEntity> layerRenderer
    ) {
        super(entityRendererPipeline, layerRenderer);
    }

    @Override
    protected void applyRotations(
            HologramEntity animatable,
            PoseStack poseStack,
            float ageInTicks,
            float rotationYaw,
            float partialTick,
            float nativeScale
    ) {
        float bodyRot = animatable.getYRot();
        super.applyRotations(animatable, poseStack, ageInTicks, bodyRot, partialTick, nativeScale);
    }
}
