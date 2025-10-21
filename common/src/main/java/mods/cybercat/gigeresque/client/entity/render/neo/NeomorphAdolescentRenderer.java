package mods.cybercat.gigeresque.client.entity.render.neo;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.AlienModelRenderer;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.neo.NeomorphAdolescentAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.neo.NeomorphAdolescentAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphAdolescentEntity;

public class NeomorphAdolescentRenderer extends AzEntityRenderer<NeomorphAdolescentEntity> {

    public NeomorphAdolescentRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<NeomorphAdolescentEntity>builder(
                EntityModels.NEOMORPH_ADOLESCENT,
                EntityTextures.NEOMORPH_ADOLESCENT
            )
                .setModelRenderer(AlienModelRenderer::new)
                .setAnimatorProvider(NeomorphAdolescentAnimator::new)
                .setRenderEntry(renderEntry -> {
                    NeomorphAdolescentAnimManager.handleAnimations(renderEntry.animatable());
                    return renderEntry;
                })
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.5F)
                .setScale(neoEntity -> 1.0f + (neoEntity.getGrowth() / neoEntity.getMaxGrowth()) / 5f)
                .build(),
            context
        );
    }
}
