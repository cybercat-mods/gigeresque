package mods.cybercat.gigeresque.client.entity.render.templebeast;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.AlienModelRenderer;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.templebeast.MoonlightTempleBeastAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.templebeast.MoonlightTempleBeastAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.MoonlightHorrorTempleBeastEntity;

public class MoonlightHorrorTempleBeastEntityRenderer extends AzEntityRenderer<MoonlightHorrorTempleBeastEntity> {

    public MoonlightHorrorTempleBeastEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<MoonlightHorrorTempleBeastEntity>builder(
                (nullEntity, animatable) -> EntityModels.MOONLIGHTHORRORTEMPLEBEAST,
                (nullEntity, animatable) -> {
                    if (animatable.stasisManager.isStasis()) {
                        return EntityTextures.MOONLIGHTHORRORTEMPLEBEAST_STATIS;
                    }
                    return EntityTextures.MOONLIGHTHORRORTEMPLEBEAST;
                }
            )
                .setModelRenderer(AlienModelRenderer::new)
                .setAnimatorProvider(MoonlightTempleBeastAnimator::new)
                .setRenderEntry(renderEntry -> {
                    MoonlightTempleBeastAnimManager.handleAnimations(renderEntry.animatable());
                    return renderEntry;
                })
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 1.0f;
    }
}
