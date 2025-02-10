package mods.cybercat.gigeresque.client.entity.render.hellmorphs;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.hellmorphs.HellmorphAnimManager;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.hellmorphs.HellmorphRunnerAnimator;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.HellmorphRunnerEntity;
import org.jetbrains.annotations.NotNull;

public class HellmorphRunnerEntityRenderer extends AzEntityRenderer<HellmorphRunnerEntity> {

    public HellmorphRunnerEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<HellmorphRunnerEntity>builder(
                EntityModels.HELLMORPH_RUNNER,
                EntityTextures.HELLMORPH_RUNNER
            )
                .setAnimatorProvider(HellmorphRunnerAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 1.0f;
    }

    @Override
    public void render(@NotNull HellmorphRunnerEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        HellmorphAnimManager.handleAnimations(entity);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
