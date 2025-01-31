package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.animators.runner.RunnerbursterAnimator;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerbursterEntity;

public class RunnerbursterEntityRenderer extends AzEntityRenderer<RunnerbursterEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/runnerburster/runnerburster.geo.json");

    private static final ResourceLocation TEX = Constants.modResource("textures/entity/runnerburster/runnerburster.png");

    public RunnerbursterEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<RunnerbursterEntity>builder(MODEL, TEX)
                .setAnimatorProvider(RunnerbursterAnimator::new)
                .setDeathMaxRotation(0.0F)
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
        float scaleFactor = 1.0f + (entity.getGrowth() / entity.getMaxGrowth());
        poseStack.pushPose();
        poseStack.scale(scaleFactor, scaleFactor, scaleFactor);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
