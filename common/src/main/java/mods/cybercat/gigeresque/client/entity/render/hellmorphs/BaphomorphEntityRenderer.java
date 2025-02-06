package mods.cybercat.gigeresque.client.entity.render.hellmorphs;

import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.hellmorphs.BaphomorphAnimator;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.BaphomorphEntity;

public class BaphomorphEntityRenderer extends AzEntityRenderer<BaphomorphEntity> {

    public BaphomorphEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<BaphomorphEntity>builder(
                EntityModels.BAPHOMORPH,
                EntityTextures.BAPHOMORPH
            )
                .setAnimatorProvider(BaphomorphAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 1.0f;
    }
}
