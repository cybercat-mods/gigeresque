package mods.cybercat.gigeresque.client.entity.render.feature;

import mod.azure.azurelib.core.object.Color;
import mod.azure.azurelib.rewrite.model.AzBone;
import mod.azure.azurelib.rewrite.render.AzRendererPipeline;
import mod.azure.azurelib.rewrite.render.AzRendererPipelineContext;
import mod.azure.azurelib.rewrite.render.layer.AzRenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;

public class EggmorphGeoFeatureRenderer<T extends Entity> implements AzRenderLayer<T> {

    private int fovEggticker = 0;

    @Override
    public void preRender(AzRendererPipelineContext<T> context) {}

    @Override
    public void render(AzRendererPipelineContext<T> context) {
        T animatable = (T) context.animatable();
        AzRendererPipeline<T> renderPipeline = context.rendererPipeline();
        var bufferSource = context.multiBufferSource();
        ResourceLocation textureLocation = renderPipeline.config().textureLocation(animatable);
        var renderLayer = EggmorphFeatureRenderer.getEggmorphLayerTexture(textureLocation).renderLayer;
        if (animatable instanceof LivingEntity livingEntity && livingEntity.hasEffect(GigStatusEffects.EGGMORPHING)) {
            fovEggticker++;
            var progress = Math.max(0, Math.min(fovEggticker / CommonMod.config.getEggmorphTickTimer(), 1));
            var vertexConsumer = bufferSource.getBuffer(renderLayer);
            vertexConsumer.setColor(1, 1, 1, progress);
            context.setRenderColor(Color.ofRGBA(1, 1, 1, progress).getColor());
            renderPipeline.reRender(context);
        }
    }

    @Override
    public void renderForBone(AzRendererPipelineContext<T> context, AzBone bone) {}
}
