package mods.cybercat.gigeresque.client.entity.render.runner;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.render.feature.ClassicAgingOverLay;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.runner.RunnerAlienAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.runner.RunnerAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerAlienEntity;

public class RunnerAlienEntityRenderer extends AzEntityRenderer<RunnerAlienEntity> {

    public RunnerAlienEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<RunnerAlienEntity>builder($ -> EntityModels.RUNNER_ALIEN, xeno -> {
                if (xeno.stasisManager.isStasis()) {
                    return EntityTextures.RUNNER_ALIEN_STASIS;
                }

                return EntityTextures.RUNNER_ALIEN;
            })
                .setAnimatorProvider(RunnerAlienAnimator::new)
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.5F)
                .addRenderLayer(new ClassicAgingOverLay<>(EntityTextures.RUNNER_ALIEN_YOUNG))
                .setScale(runnerAlienEntity -> {
                    var scaleFactor = 0.8f + ((runnerAlienEntity.getGrowth() / runnerAlienEntity.getMaxGrowth()) / 5f);

                    return Math.min(scaleFactor, 1.0F);
                })
                .build(),
            context
        );
    }

    @Override
    public void render(
        @NotNull RunnerAlienEntity entity,
        float entityYaw,
        float partialTicks,
        @NotNull PoseStack stack,
        @NotNull MultiBufferSource bufferIn,
        int packedLightIn
    ) {
        RunnerAnimManager.handleAnimations(entity);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }
}
