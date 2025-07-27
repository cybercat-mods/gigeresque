package mods.cybercat.gigeresque.client.entity.render.runner;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mods.cybercat.gigeresque.client.entity.render.feature.BloodLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.runner.RunnerbursterAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.runner.RunnerbursterAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerbursterEntity;

public class RunnerbursterEntityRenderer extends AzEntityRenderer<RunnerbursterEntity> {

    public RunnerbursterEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<RunnerbursterEntity>builder(EntityModels.RUNNERBURSTER, EntityTextures.RUNNERBURSTER)
                .setAnimatorProvider(RunnerbursterAnimator::new)
                .setDeathMaxRotation(0.0F)
                .addRenderLayer(new BloodLayer<>())
                .setScale(bursterEntity -> 1.0f + (bursterEntity.getGrowth() / bursterEntity.getMaxGrowth()))
                .build(),
            context
        );
        this.shadowRadius = 0.3f;
    }

    @Override
    public void render(
        @NotNull RunnerbursterEntity entity,
        float entityYaw,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource bufferSource,
        int packedLight
    ) {
        RunnerbursterAnimManager.handleAnimations(entity);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
