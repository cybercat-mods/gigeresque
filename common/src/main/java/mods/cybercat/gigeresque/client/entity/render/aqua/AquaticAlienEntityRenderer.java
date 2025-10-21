package mods.cybercat.gigeresque.client.entity.render.aqua;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.AlienModelRenderer;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.aqua.AquaticAlienAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.aqua.AquaticAlienAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticAlienEntity;

public class AquaticAlienEntityRenderer extends AzEntityRenderer<AquaticAlienEntity> {

    public AquaticAlienEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<AquaticAlienEntity>builder(
                EntityModels.AQUATIC_ALIEN,
                EntityTextures.AQUATIC_ALIEN
            )
                .setModelRenderer(AlienModelRenderer::new)
                .setAnimatorProvider(AquaticAlienAnimator::new)
                .setRenderEntry(renderContext -> {
                    AquaticAlienAnimManager.handleAnimations(renderContext.animatable());
                    return renderContext;
                })
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.5F)
                .build(),
            context
        );
    }
}
