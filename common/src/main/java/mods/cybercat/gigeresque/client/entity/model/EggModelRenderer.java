package mods.cybercat.gigeresque.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.common.render.AzLayerRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityModelRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererPipeline;

import java.util.UUID;

import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;

public class EggModelRenderer extends AzEntityModelRenderer<AlienEggEntity> {

    public EggModelRenderer(
        AzEntityRendererPipeline<AlienEggEntity> entityRendererPipeline,
        AzLayerRenderer<UUID, AlienEggEntity> layerRenderer
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
    ) {}
}
