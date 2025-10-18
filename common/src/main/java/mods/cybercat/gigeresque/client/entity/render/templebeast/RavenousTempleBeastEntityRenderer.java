package mods.cybercat.gigeresque.client.entity.render.templebeast;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.templebeast.RavenousTempleBeastAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.templebeast.RavenousTempleBeastAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.RavenousTempleBeastEntity;

public class RavenousTempleBeastEntityRenderer extends AzEntityRenderer<RavenousTempleBeastEntity> {

    public RavenousTempleBeastEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<RavenousTempleBeastEntity>builder(
                animatable -> EntityModels.RAVENOUSTEMPLEBEAST,
                animatable -> {
                    if (animatable.stasisManager.isStasis()) {
                        return EntityTextures.RAVENOUSTEMPLEBEAST_STATIS;
                    }
                    return EntityTextures.RAVENOUSTEMPLEBEAST;
                }
            )
                .setAnimatorProvider(RavenousTempleBeastAnimator::new)
                .setRenderEntry(renderEntry -> {
                    RavenousTempleBeastAnimManager.handleAnimations(renderEntry.animatable());
                    return renderEntry;
                })
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(1.0F)
                .build(),
            context
        );
    }
}
