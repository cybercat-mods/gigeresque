package mods.cybercat.gigeresque.client.entity.render.feature;

import mod.azure.azurelib.common.model.AzBone;
import mod.azure.azurelib.common.render.AzRendererPipeline;
import mod.azure.azurelib.common.render.AzRendererPipelineContext;
import mod.azure.azurelib.common.render.layer.AzRenderLayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class ClassicAgingAzLayer<T extends AlienEntity> implements AzRenderLayer<UUID, T> {

    private ResourceLocation textureLocation;

    public ClassicAgingAzLayer(ResourceLocation textureLocation) {
        this.textureLocation = textureLocation;
    }

    @Override
    public void preRender(AzRendererPipelineContext<UUID, T> context) {}

    @Override
    public void render(AzRendererPipelineContext<UUID, T> context) {
        T animatable = (T) context.animatable();
        AzRendererPipeline<UUID, T> renderPipeline = context.rendererPipeline();
        var rendertype = RenderType.entityTranslucentCull(textureLocation);

        if (animatable.getGrowth() < animatable.getMaxGrowth() && animatable.isAlive()) {
            context.setRenderType(rendertype);
            context.setVertexConsumer(context.multiBufferSource().getBuffer(rendertype));

            var progress = (animatable.getMaxGrowth() - animatable.getGrowth()) / animatable.getMaxGrowth();
            var alpha = (int) (progress * 0xFF) << 24;
            var color = (context.renderColor() & 0xFFFFFF) | alpha;

            context.setRenderColor(color);
            renderPipeline.reRender(context);
        }
    }

    @Override
    public void renderForBone(AzRendererPipelineContext<UUID, T> context, AzBone bone) {}
}
