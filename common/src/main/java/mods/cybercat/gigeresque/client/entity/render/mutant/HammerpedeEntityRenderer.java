package mods.cybercat.gigeresque.client.entity.render.mutant;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.mutant.HammerpedeAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.mutant.HammerpedeAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.mutant.HammerpedeEntity;

public class HammerpedeEntityRenderer extends AzEntityRenderer<HammerpedeEntity> {

    public HammerpedeEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<HammerpedeEntity>builder(
                EntityModels.HAMMERPEDE,
                EntityTextures.HAMMERPEDE
            )
                .setAnimatorProvider(HammerpedeAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(
        @NotNull HammerpedeEntity entity,
        float entityYaw,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource bufferSource,
        int packedLight
    ) {
        HammerpedeAnimManager.handleAnimations(entity);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
