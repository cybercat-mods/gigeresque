package mods.cybercat.gigeresque.client.entity.render.misc;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.common.render.layer.AzAutoGlowingLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.AlienModelRenderer;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.misc.SpitterAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.misc.SpitterAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.misc.SpitterEntity;

public class SpitterRenderer extends AzEntityRenderer<SpitterEntity> {

    public SpitterRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<SpitterEntity>builder(
                EntityModels.SPITTER,
                EntityTextures.SPITTER
            )
                .setModelRenderer(AlienModelRenderer::new)
                .setAnimatorProvider(SpitterAnimator::new)
                .setRenderEntry(renderEntry -> {
                    SpitterAnimManager.handleAnimations(renderEntry.animatable());
                    return renderEntry;
                })
                .addRenderLayer(new AzAutoGlowingLayer<>())
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.5F)
                .setScale(spitterEntity -> 0.5f + ((spitterEntity.getGrowth() / spitterEntity.getMaxGrowth()) / 5f))
                .build(),
            context
        );
    }
}
