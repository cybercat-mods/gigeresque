package mods.cybercat.gigeresque.client.entity.render.aqua;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.aqua.AquaticAlienAnimManager;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.aqua.AquaticAlienAnimator;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticAlienEntity;
import org.jetbrains.annotations.NotNull;

public class AquaticAlienEntityRenderer extends AzEntityRenderer<AquaticAlienEntity> {

    public AquaticAlienEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<AquaticAlienEntity>builder(
                EntityModels.AQUATIC_ALIEN,
                EntityTextures.AQUATIC_ALIEN
            )
                .setAnimatorProvider(AquaticAlienAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(@NotNull AquaticAlienEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        AquaticAlienAnimManager.handleAnimations(entity);
    }
}
