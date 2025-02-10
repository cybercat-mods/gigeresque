package mods.cybercat.gigeresque.client.entity.render.mutant;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.mutant.PopperAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.mutant.PopperAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;

public class PopperEntityRenderer extends AzEntityRenderer<PopperEntity> {

    public PopperEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<PopperEntity>builder(
                EntityModels.POPPER,
                EntityTextures.POPPER
            )
                .setAnimatorProvider(PopperAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(
        @NotNull PopperEntity entity,
        float entityYaw,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource bufferSource,
        int packedLight
    ) {
        PopperAnimManager.handleAnimations(entity);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
