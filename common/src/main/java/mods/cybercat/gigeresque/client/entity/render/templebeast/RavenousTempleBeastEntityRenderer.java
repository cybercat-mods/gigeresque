package mods.cybercat.gigeresque.client.entity.render.templebeast;

import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.templebeast.RavenousTempleBeastAnimator;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.RavenousTempleBeastEntity;

public class RavenousTempleBeastEntityRenderer extends AzEntityRenderer<RavenousTempleBeastEntity> {

    public RavenousTempleBeastEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<RavenousTempleBeastEntity>builder(
                $ -> EntityModels.RAVENOUSTEMPLEBEAST,
                stalker -> {
                    if (stalker.stasisManager.isStasis()) {
                        return EntityTextures.RAVENOUSTEMPLEBEAST_STATIS;
                    }
                    return EntityTextures.RAVENOUSTEMPLEBEAST;
                }
            )
                .setAnimatorProvider(RavenousTempleBeastAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 1.0f;
    }
}
