package mods.cybercat.gigeresque.client.entity.render.feature;

import mod.azure.azurelib.rewrite.model.AzBone;
import mod.azure.azurelib.rewrite.render.AzRendererPipeline;
import mod.azure.azurelib.rewrite.render.AzRendererPipelineContext;
import mod.azure.azurelib.rewrite.render.layer.AzRenderLayer;
import net.minecraft.client.renderer.RenderType;

import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class BloodLayer<T extends AlienEntity> implements AzRenderLayer<T> {

    @Override
    public void preRender(AzRendererPipelineContext<T> context) {}

    @Override
    public void render(AzRendererPipelineContext<T> context) {
        T animatable = context.animatable();
        AzRendererPipeline<T> renderPipeline = context.rendererPipeline();
        var rendertype = RenderType.entityTranslucentCull(EntityTextures.CHESTBURSTER_BLOOD);
        var maxGrowth = animatable.getMaxGrowth() / 2;
        if (animatable.getGrowth() < maxGrowth && animatable.isAlive()) {
            context.setRenderType(rendertype);
            context.setVertexConsumer(context.multiBufferSource().getBuffer(rendertype));
            var progress = (maxGrowth - animatable.getGrowth()) / maxGrowth;
            var alpha = (int) (progress * 0xFF) << 24;
            var color = (context.renderColor() & 0xFFFFFF) | alpha;
            context.setRenderColor(color);
            renderPipeline.reRender(context);
        }
    }

    @Override
    public void renderForBone(AzRendererPipelineContext<T> context, AzBone bone) {}
}
