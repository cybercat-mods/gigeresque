package mods.cybercat.gigeresque.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.AzLayerRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityModelRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererPipeline;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;

public class EggModelRenderer extends AzEntityModelRenderer<AlienEggEntity> {

    public EggModelRenderer(
        AzEntityRendererPipeline<AlienEggEntity> entityRendererPipeline,
        AzLayerRenderer<AlienEggEntity> layerRenderer
    ) {
        super(entityRendererPipeline, layerRenderer);
    }

    @Override
    protected void applyRotations(
            AlienEggEntity animatable,
        PoseStack poseStack,
        float ageInTicks,
        float rotationYaw,
        float partialTick,
        float nativeScale
    ) {
        if (!animatable.stasisManager.isStasis())
            super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }
}
