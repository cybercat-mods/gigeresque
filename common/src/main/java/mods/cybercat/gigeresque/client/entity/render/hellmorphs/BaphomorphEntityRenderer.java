package mods.cybercat.gigeresque.client.entity.render.hellmorphs;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.AlienModelRenderer;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.hellmorphs.BaphomorphAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.hellmorphs.BaphormorphAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.BaphomorphEntity;

public class BaphomorphEntityRenderer extends AzEntityRenderer<BaphomorphEntity> {

    public BaphomorphEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<BaphomorphEntity>builder(
                EntityModels.BAPHOMORPH,
                EntityTextures.BAPHOMORPH
            )
                .setModelRenderer(AlienModelRenderer::new)
                .setAnimatorProvider(BaphomorphAnimator::new)
                .setRenderEntry(renderEntry -> {
                    BaphormorphAnimManager.handleAnimations(renderEntry.animatable());
                    return renderEntry;
                })
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(1F)
                .build(),
            context
        );
    }
}
