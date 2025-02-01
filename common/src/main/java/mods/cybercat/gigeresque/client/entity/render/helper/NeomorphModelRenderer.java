package mods.cybercat.gigeresque.client.entity.render.helper;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.AzLayerRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityModelRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererPipeline;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphEntity;

public class NeomorphModelRenderer extends AzEntityModelRenderer<NeomorphEntity> {

    public NeomorphModelRenderer(AzEntityRendererPipeline<NeomorphEntity> entityRendererPipeline, AzLayerRenderer<NeomorphEntity> layerRenderer) {
        super(entityRendererPipeline, layerRenderer);
    }

    @Override
    protected void applyRotations(NeomorphEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        if (!animatable.isPassedOut())
            super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }
}
