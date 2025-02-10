package mods.cybercat.gigeresque.client.entity.render.feature;

import mod.azure.azurelib.rewrite.model.AzBone;
import mod.azure.azurelib.rewrite.render.AzRendererPipeline;
import mod.azure.azurelib.rewrite.render.AzRendererPipelineContext;
import mod.azure.azurelib.rewrite.render.layer.AzRenderLayer;
import net.minecraft.client.renderer.RenderType;

import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class ClassicAgingOverLay<T extends AlienEntity> implements AzRenderLayer<T> {

    @Override
    public void preRender(AzRendererPipelineContext<T> context) {}

    @Override
    public void render(AzRendererPipelineContext<T> context) {
        T animatable = (T) context.animatable();
        AzRendererPipeline<T> renderPipeline = context.rendererPipeline();
        var bufferSource = context.multiBufferSource();
        var rendertype = RenderType.entityTranslucentCull(EntityTextures.ALIEN_YOUNG);
        if (!(animatable.getGrowth() >= animatable.getMaxGrowth())) {
            context.setRenderType(rendertype);
            var vertexConsumer = bufferSource.getBuffer(rendertype);
            vertexConsumer.setColor(1, 1, 1, ((1200 - animatable.getGrowth()) / 1200));
            renderPipeline.reRender(context);
        }
    }

    @Override
    public void renderForBone(AzRendererPipelineContext<T> context, AzBone bone) {}
}
