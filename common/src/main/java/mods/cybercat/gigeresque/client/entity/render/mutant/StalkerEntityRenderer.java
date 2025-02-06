package mods.cybercat.gigeresque.client.entity.render.mutant;

import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.mutant.StalkerAnimator;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;

public class StalkerEntityRenderer extends AzEntityRenderer<StalkerEntity> {

    private static final RenderType NORMAL_RENDER_TYPE = RenderType.entityCutoutNoCull(EntityTextures.STALKER);

    private static final RenderType TRANSPARENT_RENDER_TYPE = RenderType.entityTranslucentCull(EntityTextures.STALKER_TRANSPARENT);

    public StalkerEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<StalkerEntity>builder(
                $ -> EntityModels.STALKER,
                stalker -> {
                    if (!stalker.moveAnalysis.isMoving()) {
                        return EntityTextures.STALKER_TRANSPARENT;
                    }
                    return EntityTextures.STALKER;
                }
            )
                .setAnimatorProvider(StalkerAnimator::new)
                .setDeathMaxRotation(0.0F)
                .setRenderType(stalker -> !stalker.moveAnalysis.isMoving() ? TRANSPARENT_RENDER_TYPE : NORMAL_RENDER_TYPE)
                .build(),
            context
        );
    }

    @Override
    protected float getShadowRadius(@NotNull StalkerEntity entity) {
        return entity.walkAnimation.speedOld < 0.35F && !entity.swinging ? 0.0f : 1.0f;
    }
}
