package mods.cybercat.gigeresque.client.entity.render.aqua;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.aqua.AquaticAlienAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.aqua.AquaticAlienAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticAlienEntity;

public class AquaticAlienEntityRenderer extends AzEntityRenderer<AquaticAlienEntity> {

    public AquaticAlienEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<AquaticAlienEntity>builder(
                EntityModels.AQUATIC_ALIEN,
                EntityTextures.AQUATIC_ALIEN
            )
                .setAnimatorProvider(AquaticAlienAnimator::new)
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.5F)
                .build(),
            context
        );
    }

    @Override
    public void render(
        @NotNull AquaticAlienEntity entity,
        float entityYaw,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource bufferSource,
        int packedLight
    ) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        AquaticAlienAnimManager.handleAnimations(entity);
    }
}
