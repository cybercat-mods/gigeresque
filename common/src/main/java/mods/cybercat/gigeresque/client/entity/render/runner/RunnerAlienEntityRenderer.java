package mods.cybercat.gigeresque.client.entity.render.runner;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.runner.RunnerAlienAnimator;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerAlienEntity;

public class RunnerAlienEntityRenderer extends AzEntityRenderer<RunnerAlienEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/runner_alien/runner_alien.geo.json");

    public RunnerAlienEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<RunnerAlienEntity>builder(
                MODEL,
                EntityTextures.RUNNER_ALIEN
            )
                .setAnimatorProvider(RunnerAlienAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(
        RunnerAlienEntity entity,
        float entityYaw,
        float partialTicks,
        PoseStack stack,
        @NotNull MultiBufferSource bufferIn,
        int packedLightIn
    ) {
        float scaleFactor = 0.5f + ((entity.getGrowth() / entity.getMaxGrowth()) / 5f);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }
}
