package mods.cybercat.gigeresque.client.entity.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.rewrite.render.layer.AzAutoGlowingLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.misc.SpitterAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.misc.SpitterAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.misc.SpitterEntity;

public class SpitterRenderer extends AzEntityRenderer<SpitterEntity> {

    public SpitterRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<SpitterEntity>builder(
                EntityModels.SPITTER,
                EntityTextures.SPITTER
            )
                .setAnimatorProvider(SpitterAnimator::new)
                .addRenderLayer(new AzAutoGlowingLayer<>())
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.5F)
                .setScale(spitterEntity -> 0.5f + ((spitterEntity.getGrowth() / spitterEntity.getMaxGrowth()) / 5f))
                .build(),
            context
        );
    }

    @Override
    public void render(
        @NotNull SpitterEntity entity,
        float entityYaw,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource bufferSource,
        int packedLight
    ) {
        SpitterAnimManager.handleAnimations(entity);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
