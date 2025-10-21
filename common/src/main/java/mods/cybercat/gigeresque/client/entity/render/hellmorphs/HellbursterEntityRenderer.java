package mods.cybercat.gigeresque.client.entity.render.hellmorphs;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.AlienModelRenderer;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.render.feature.BloodAzLayer;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.hellmorphs.HellbursterAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.hellmorphs.HellbursterAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.HellbursterEntity;

public class HellbursterEntityRenderer extends AzEntityRenderer<HellbursterEntity> {

    public HellbursterEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<HellbursterEntity>builder(EntityModels.HELLBURSTER, EntityTextures.HELLBURSTER)
                .setModelRenderer(AlienModelRenderer::new)
                .setAnimatorProvider(HellbursterAnimator::new)
                .setRenderEntry(renderEntry -> {
                    HellbursterAnimManager.handleAnimations(renderEntry.animatable());
                    return renderEntry;
                })
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.3F)
                .addRenderLayer(new BloodAzLayer<>())
                .setScale(bursterEntity -> 1.0f + (bursterEntity.getGrowth() / bursterEntity.getMaxGrowth()))
                .build(),
            context
        );
    }
}
