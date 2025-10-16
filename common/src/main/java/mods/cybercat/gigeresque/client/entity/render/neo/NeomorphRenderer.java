package mods.cybercat.gigeresque.client.entity.render.neo;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.AlienModelRenderer;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.neo.NeomorphAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.neo.NeomorphAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphEntity;

public class NeomorphRenderer extends AzEntityRenderer<NeomorphEntity> {

    public NeomorphRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<NeomorphEntity>builder(
                EntityModels.NEOMORPH,
                EntityTextures.NEOMORPH
            )
                .setModelRenderer(AlienModelRenderer::new)
                .setAnimatorProvider(NeomorphAnimator::new)
                .setRenderEntry(renderEntry -> {
                    NeomorphAnimManager.handleAnimations(renderEntry.animatable());
                    return renderEntry;
                })
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.5F)
                .setScale(0.76F)
                .build(),
            context
        );
    }
}
