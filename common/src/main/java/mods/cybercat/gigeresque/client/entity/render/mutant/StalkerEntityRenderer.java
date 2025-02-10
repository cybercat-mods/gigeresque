package mods.cybercat.gigeresque.client.entity.render.mutant;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.mutant.StalkerAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.mutant.StalkerAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;

public class StalkerEntityRenderer extends AzEntityRenderer<StalkerEntity> {

    private static final RenderType NORMAL_RENDER_TYPE = RenderType.entityCutoutNoCull(EntityTextures.STALKER);

    private static final RenderType TRANSPARENT_RENDER_TYPE = RenderType.entityTranslucentCull(EntityTextures.STALKER_TRANSPARENT);

    public StalkerEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<StalkerEntity>builder(
                $ -> EntityModels.STALKER,
                stalker -> {
                    if (stalker.isAggressive()) {
                        return EntityTextures.STALKER_TRANSPARENT;
                    }
                    return EntityTextures.STALKER;
                }
            )
                .setAnimatorProvider(StalkerAnimator::new)
                .setDeathMaxRotation(0.0F)
                .setRenderType(stalker -> stalker.isAggressive() ? TRANSPARENT_RENDER_TYPE : NORMAL_RENDER_TYPE)
                .build(),
            context
        );
    }

    @Override
    protected float getShadowRadius(@NotNull StalkerEntity entity) {
        return entity.walkAnimation.speedOld < 0.35F && !entity.swinging ? 0.0f : 1.0f;
    }

    @Override
    public void render(
        @NotNull StalkerEntity entity,
        float entityYaw,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource bufferSource,
        int packedLight
    ) {
        StalkerAnimManager.handleAnimations(entity);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
