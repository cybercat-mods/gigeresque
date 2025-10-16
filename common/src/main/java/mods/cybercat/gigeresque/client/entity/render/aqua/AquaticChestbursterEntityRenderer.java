package mods.cybercat.gigeresque.client.entity.render.aqua;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.AlienModelRenderer;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.render.feature.BloodAzLayer;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.aqua.AquaticChestbursterAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.aqua.AquaticChestbursterAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticChestbursterEntity;

public class AquaticChestbursterEntityRenderer extends AzEntityRenderer<AquaticChestbursterEntity> {

    public AquaticChestbursterEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<AquaticChestbursterEntity>builder(EntityModels.AQUATICBURSTER, EntityTextures.AQUATICBURSTER)
                .setModelRenderer(AlienModelRenderer::new)
                .setAnimatorProvider(AquaticChestbursterAnimator::new)
                .setRenderEntry(renderContext -> {
                    AquaticChestbursterAnimManager.handleAnimations(renderContext.animatable());
                    return renderContext;
                })
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.1F)
                .addRenderLayer(new BloodAzLayer<>())
                .setScale(bursterEntity -> 1.0f + (bursterEntity.getGrowth() / bursterEntity.getMaxGrowth()))
                .build(),
            context
        );
    }
}
