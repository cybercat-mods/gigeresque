package mods.cybercat.gigeresque.client.entity.render.templebeast;

import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.templebeast.RavenousTempleBeastAnimator;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.RavenousTempleBeastEntity;

public class RavenousTempleBeastEntityRenderer extends AzEntityRenderer<RavenousTempleBeastEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/ravenoustemplebeast/ravenoustemplebeast.geo.json");

    public RavenousTempleBeastEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<RavenousTempleBeastEntity>builder(
                $ -> MODEL,
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
