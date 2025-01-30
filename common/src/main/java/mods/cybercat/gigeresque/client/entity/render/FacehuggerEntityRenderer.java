package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.rewrite.render.AzLayerRenderer;
import mod.azure.azurelib.rewrite.render.AzModelRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererPipeline;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.render.helper.FacehuggerModelRenderer;
import mods.cybercat.gigeresque.common.entity.animators.classic.FacehuggerAnimator;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;

public class FacehuggerEntityRenderer extends AzEntityRenderer<FacehuggerEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/facehugger/facehugger.geo.json");

    private static final ResourceLocation TEX = Constants.modResource("textures/entity/facehugger/facehugger.png");

    public FacehuggerEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<FacehuggerEntity>builder(MODEL, TEX)
                .setAnimatorProvider(FacehuggerAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 0.25f;
    }

    @Override
    protected AzEntityRendererPipeline<FacehuggerEntity> createPipeline(AzEntityRendererConfig<FacehuggerEntity> config) {
        return new AzEntityRendererPipeline<>(config, this) {

            @Override
            protected AzModelRenderer<FacehuggerEntity> createModelRenderer(AzLayerRenderer<FacehuggerEntity> layerRenderer) {
                return new FacehuggerModelRenderer(this, layerRenderer);
            }
        };
    }
}
