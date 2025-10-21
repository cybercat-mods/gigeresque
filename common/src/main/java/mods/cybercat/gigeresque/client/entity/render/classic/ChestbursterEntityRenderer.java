package mods.cybercat.gigeresque.client.entity.render.classic;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.AlienModelRenderer;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.render.feature.BloodAzLayer;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.classic.ChestbursterAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.classic.ChestbursterAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;

public class ChestbursterEntityRenderer extends AzEntityRenderer<ChestbursterEntity> {

    public ChestbursterEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<ChestbursterEntity>builder(EntityModels.CHESTBURSTER, EntityTextures.CHESTBURSTER)
                .setModelRenderer(AlienModelRenderer::new)
                .setAnimatorProvider(ChestbursterAnimator::new)
                .setRenderEntry(renderEntry -> {
                    ChestbursterAnimManager.handleAnimations(renderEntry.animatable());
                    return renderEntry;
                })
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.1F)
                .addRenderLayer(new BloodAzLayer<>())
                .setScale(bursterEntity -> 1.0f + ((bursterEntity.getGrowth() / bursterEntity.getMaxGrowth()) / 4.0f))
                .build(),
            context
        );
    }
}
