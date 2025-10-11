package mods.cybercat.gigeresque.client.entity.render.classic;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.common.render.entity.AzEntityRendererPipeline;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.ClassicModelRenderer;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.render.feature.ClassicAgingAzLayer;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.classic.ClassicAlienAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.classic.ClassicAlienAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;

public class AlienEntityRenderer extends AzEntityRenderer<ClassicAlienEntity> {

    public AlienEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<ClassicAlienEntity>builder($ -> EntityModels.ALIEN, xeno -> {
                if (xeno.stasisManager.isStasis()) {
                    return EntityTextures.ALIEN_STASIS;
                }

                return EntityTextures.ALIEN;
            })
                .setAnimatorProvider(ClassicAlienAnimator::new)
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.5F)
                .setScale(
                    classicAlienEntity -> Math.clamp(
                        0.8f + ((classicAlienEntity.getGrowth() / classicAlienEntity.getMaxGrowth()) / 5f),
                        0,
                        1
                    )
                )
                .addRenderLayer(new ClassicAgingAzLayer<>(EntityTextures.ALIEN_YOUNG))
                .setScale(classicAlienEntity -> {
                    var scaleFactor = 0.8f + ((classicAlienEntity.getGrowth() / classicAlienEntity.getMaxGrowth()) / 5f);

                    return Math.min(scaleFactor, 1.0F);
                })
                .setModelRenderer(
                    (
                        pipelineContext,
                        layerRenderer
                    ) -> new ClassicModelRenderer(
                        (AzEntityRendererPipeline<ClassicAlienEntity>) pipelineContext,
                        layerRenderer
                    )
                )
                .setRenderEntry(renderEntry -> {
                    ClassicAlienAnimManager.handleAnimations(renderEntry.animatable());
                    return renderEntry;
                })
                .build(),
            context
        );
    }
}
