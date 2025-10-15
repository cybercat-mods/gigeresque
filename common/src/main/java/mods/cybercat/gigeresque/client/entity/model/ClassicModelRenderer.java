package mods.cybercat.gigeresque.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.common.render.AzLayerRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityModelRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererPipeline;

import java.util.UUID;

import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;

public class ClassicModelRenderer extends AzEntityModelRenderer<ClassicAlienEntity> {

    public ClassicModelRenderer(
        AzEntityRendererPipeline<ClassicAlienEntity> entityRendererPipeline,
        AzLayerRenderer<UUID, ClassicAlienEntity> layerRenderer
    ) {
        super(entityRendererPipeline, layerRenderer);
    }

    @Override
    protected void applyRotations(
        ClassicAlienEntity animatable,
        PoseStack poseStack,
        float ageInTicks,
        float rotationYaw,
        float partialTick,
        float nativeScale
    ) {
        if (!animatable.stasisManager.isStasis() || !animatable.isDeadOrDying())
            super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }
}
