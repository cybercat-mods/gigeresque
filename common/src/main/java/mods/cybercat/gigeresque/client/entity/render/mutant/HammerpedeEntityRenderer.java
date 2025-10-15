package mods.cybercat.gigeresque.client.entity.render.mutant;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.mutant.HammerpedeAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.mutant.HammerpedeAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.mutant.HammerpedeEntity;

public class HammerpedeEntityRenderer extends AzEntityRenderer<HammerpedeEntity> {

    public HammerpedeEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<HammerpedeEntity>builder(
                EntityModels.HAMMERPEDE,
                EntityTextures.HAMMERPEDE
            )
                .setAnimatorProvider(HammerpedeAnimator::new)
                .setRenderEntry(renderEntry -> {
                    HammerpedeAnimManager.handleAnimations(renderEntry.animatable());
                    return renderEntry;
                })
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.5F)
                .build(),
            context
        );
    }
}
