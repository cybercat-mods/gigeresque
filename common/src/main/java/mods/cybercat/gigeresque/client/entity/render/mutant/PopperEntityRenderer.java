package mods.cybercat.gigeresque.client.entity.render.mutant;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.mutant.PopperAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.mutant.PopperAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;

public class PopperEntityRenderer extends AzEntityRenderer<PopperEntity> {

    public PopperEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<PopperEntity>builder(
                EntityModels.POPPER,
                EntityTextures.POPPER
            )
                .setAnimatorProvider(PopperAnimator::new)
                .setRenderEntry(renderEntry -> {
                    PopperAnimManager.handleAnimations(renderEntry.animatable());
                    return renderEntry;
                })
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.5F)
                .build(),
            context
        );
    }
}
