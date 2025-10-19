package mods.cybercat.gigeresque.client.entity.render.classic;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.common.render.entity.AzEntityRendererPipeline;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.EggModelRenderer;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.classic.AlienEggAnimator;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.helper.states.EggStates;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;

public class EggEntityRenderer extends AzEntityRenderer<AlienEggEntity> {

    private static final RenderType EGG_RENDER_TYPE = RenderType.entityCutoutNoCull(EntityTextures.EGG);

    private static final RenderType EGG_ACTIVE_RENDER_TYPE = RenderType.entityTranslucentCull(EntityTextures.EGG_ACTIVE);

    public EggEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<AlienEggEntity>builder(
                animatable -> EntityModels.EGG,
                animatable -> animatable.getEggState() == EggStates.HATCHING.ordinal() || animatable
                    .getEggState() == EggStates.HATCHED.ordinal() ? EntityTextures.EGG_ACTIVE : EntityTextures.EGG
            )
                .setAnimatorProvider(AlienEggAnimator::new)
                .setRenderEntry(renderEntry -> {
                    var entity = renderEntry.animatable();
                    if (entity.isDeadOrDying()) {
                        GigCommonMethods.setAnimation(entity.animationDispatcher::sendDeath);
                    } else if (entity.getEggState() == EggStates.HATCHING.ordinal()) {
                        GigCommonMethods.setAnimation(entity.animationDispatcher::sendHatching);
                    } else if (entity.getEggState() == EggStates.HATCHED.ordinal()) {
                        GigCommonMethods.setAnimation(entity.animationDispatcher::sendHatchEmpty);
                    } else {
                        GigCommonMethods.setAnimation(entity.animationDispatcher::sendIdle);
                    }
                    return renderEntry;
                })
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.5F)
                .setRenderType(
                    (nullEntity, animatable) -> animatable.getEggState() == EggStates.HATCHING.ordinal() || animatable
                        .getEggState() == EggStates.HATCHED.ordinal() && animatable.isAlive()
                            ? EGG_ACTIVE_RENDER_TYPE
                            : EGG_RENDER_TYPE
                )
                .setModelRenderer(
                    (
                        pipelineContext,
                        layerRenderer
                    ) -> new EggModelRenderer(
                        (AzEntityRendererPipeline<AlienEggEntity>) pipelineContext,
                        layerRenderer
                    )
                )
                .build(),
            context
        );
    }
}
