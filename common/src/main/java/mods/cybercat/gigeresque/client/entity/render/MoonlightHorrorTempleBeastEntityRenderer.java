package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.templebeast.MoonlightTempleBeastAnimator;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.MoonlightHorrorTempleBeastEntity;

public class MoonlightHorrorTempleBeastEntityRenderer extends AzEntityRenderer<MoonlightHorrorTempleBeastEntity> {

    private static final ResourceLocation MODEL = Constants.modResource(
        "geo/entity/moonlighthorrortemplebeast/moonlighthorrortemplebeast.geo.json"
    );

    public MoonlightHorrorTempleBeastEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<MoonlightHorrorTempleBeastEntity>builder(
                $ -> MODEL,
                stalker -> {
                    if (stalker.isPassedOut()) {
                        return EntityTextures.MOONLIGHTHORRORTEMPLEBEAST_STATIS;
                    }
                    return EntityTextures.MOONLIGHTHORRORTEMPLEBEAST;
                }
            )
                .setAnimatorProvider(MoonlightTempleBeastAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 1.0f;
    }

}
