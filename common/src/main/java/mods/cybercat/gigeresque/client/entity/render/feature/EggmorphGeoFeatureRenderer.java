package mods.cybercat.gigeresque.client.entity.render.feature;

import mod.azure.azurelib.rewrite.model.AzBone;
import mod.azure.azurelib.rewrite.render.AzRendererPipeline;
import mod.azure.azurelib.rewrite.render.AzRendererPipelineContext;
import mod.azure.azurelib.rewrite.render.layer.AzRenderLayer;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import mods.cybercat.gigeresque.CommonMod;

public class EggmorphGeoFeatureRenderer<T extends Entity> implements AzRenderLayer<T> {

    private int fovEggticker = 0;

    @Override
    public void preRender(AzRendererPipelineContext<T> context) {}

    @Override
    public void render(AzRendererPipelineContext<T> context) {
        T animatable = (T) context.animatable();
        AzRendererPipeline<T> renderPipeline = context.rendererPipeline();
        ResourceLocation textureLocation = renderPipeline.config().textureLocation(animatable);
        var renderLayer = EggmorphFeatureRenderer.getEggmorphLayerTexture(textureLocation).renderLayer;
        if (animatable instanceof LivingEntity livingEntity && livingEntity.getInBlockState().is(GigTags.NEST_CROSS_BLOCKS)) {
            fovEggticker++;
            var progress = fovEggticker / CommonMod.config.getEggmorphTickTimer();
            context.setVertexConsumer(context.multiBufferSource().getBuffer(renderLayer));
            var alpha = (int) (progress * 0xFF) << 24;
            var color = (context.renderColor() & 0xFFFFFF) | alpha;
            context.setRenderColor(color);
            renderPipeline.reRender(context);
        }
    }

    @Override
    public void renderForBone(AzRendererPipelineContext<T> context, AzBone bone) {}
}
