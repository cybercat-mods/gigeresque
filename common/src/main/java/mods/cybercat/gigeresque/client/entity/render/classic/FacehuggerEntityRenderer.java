package mods.cybercat.gigeresque.client.entity.render.classic;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.common.render.entity.AzEntityRendererPipeline;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.AlienModelRenderer;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.model.FacehuggerModelRenderer;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.classic.FacehuggerAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.classic.FacehuggerAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;

public class FacehuggerEntityRenderer extends AzEntityRenderer<FacehuggerEntity> {

    public FacehuggerEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<FacehuggerEntity>builder(EntityModels.FACEHUGGER, EntityTextures.FACEHUGGER)
                .setModelRenderer(AlienModelRenderer::new)
                .setAnimatorProvider(FacehuggerAnimator::new)
                .setRenderEntry(renderEntry -> {
                    FacehuggerAnimManager.handleAnimations(renderEntry.animatable());
                    return renderEntry;
                })
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.25F)
                .setModelRenderer(
                    (
                        pipelineContext,
                        layerRenderer
                    ) -> new FacehuggerModelRenderer(
                        (AzEntityRendererPipeline<FacehuggerEntity>) pipelineContext,
                        layerRenderer
                    )
                )
                .build(),
            context
        );
    }
}
