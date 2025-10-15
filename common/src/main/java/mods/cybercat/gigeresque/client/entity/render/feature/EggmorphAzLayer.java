package mods.cybercat.gigeresque.client.entity.render.feature;

import mod.azure.azurelib.common.model.AzBone;
import mod.azure.azurelib.common.render.AzRendererPipeline;
import mod.azure.azurelib.common.render.AzRendererPipelineContext;
import mod.azure.azurelib.common.render.layer.AzRenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.UUID;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.tags.GigTags;

public class EggmorphAzLayer<T extends Entity> implements AzRenderLayer<UUID, T> {

    private int fovEggticker = 0;

    @Override
    public void preRender(AzRendererPipelineContext<UUID, T> context) {}

    @Override
    public void render(AzRendererPipelineContext<UUID, T> context) {
        T animatable = (T) context.animatable();
        AzRendererPipeline<UUID, T> renderPipeline = context.rendererPipeline();
        ResourceLocation textureLocation = renderPipeline.config().textureLocation(context.currentEntity(), animatable);
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
    public void renderForBone(AzRendererPipelineContext<UUID, T> context, AzBone bone) {}
}
