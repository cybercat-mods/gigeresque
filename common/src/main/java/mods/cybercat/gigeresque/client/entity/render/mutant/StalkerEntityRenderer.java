package mods.cybercat.gigeresque.client.entity.render.mutant;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

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
                .setRenderEntry(renderEntry -> {
                    StalkerAnimManager.handleAnimations(renderEntry.animatable());
                    return renderEntry;
                })
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(stalkerEntity -> stalkerEntity.walkAnimation.speedOld < 0.35F && !stalkerEntity.swinging ? 0.0f : 1.0f)
                .setRenderType(stalker -> stalker.isAggressive() ? TRANSPARENT_RENDER_TYPE : NORMAL_RENDER_TYPE)
                .setAlpha(stalker -> stalker.isAggressive() ? 0.2F : 1.0F)
                .build(),
            context
        );
    }
}
