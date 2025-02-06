package mods.cybercat.gigeresque.client.entity.render.mutant;

import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.mutant.PopperAnimator;
import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;

public class PopperEntityRenderer extends AzEntityRenderer<PopperEntity> {

    public PopperEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<PopperEntity>builder(
                EntityModels.POPPER,
                EntityTextures.POPPER
            )
                .setAnimatorProvider(PopperAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 0.5f;
    }
}
