package mods.cybercat.gigeresque.client.entity.render.mutant;

import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.mutant.HammerpedeAnimator;
import mods.cybercat.gigeresque.common.entity.impl.mutant.HammerpedeEntity;

public class HammerpedeEntityRenderer extends AzEntityRenderer<HammerpedeEntity> {

    public HammerpedeEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<HammerpedeEntity>builder(
                EntityModels.HAMMERPEDE,
                EntityTextures.HAMMERPEDE
            )
                .setAnimatorProvider(HammerpedeAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 0.5f;
    }
}
