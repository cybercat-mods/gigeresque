package mods.cybercat.gigeresque.client.entity.render.runner;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.AlienModelRenderer;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.render.feature.ClassicAgingAzLayer;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.runner.RunnerAlienAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.runner.RunnerAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerAlienEntity;

public class RunnerAlienEntityRenderer extends AzEntityRenderer<RunnerAlienEntity> {

    public RunnerAlienEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<RunnerAlienEntity>builder(
                animatable -> EntityModels.RUNNER_ALIEN,
                animatable -> {
                    if (animatable.stasisManager.isStasis()) {
                        return EntityTextures.RUNNER_ALIEN_STASIS;
                    }

                    return EntityTextures.RUNNER_ALIEN;
                }
            )
                .setModelRenderer(AlienModelRenderer::new)
                .setAnimatorProvider(RunnerAlienAnimator::new)
                .setRenderEntry(renderEntry -> {
                    RunnerAnimManager.handleAnimations(renderEntry.animatable());
                    return renderEntry;
                })
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.5F)
                .addRenderLayer(new ClassicAgingAzLayer<>(EntityTextures.RUNNER_ALIEN_YOUNG))
                .setScale(runnerAlienEntity -> {
                    var scaleFactor = 0.8f + ((runnerAlienEntity.getGrowth() / runnerAlienEntity.getMaxGrowth()) / 5f);

                    return Math.min(scaleFactor, 1.0F);
                })
                .build(),
            context
        );
    }
}
