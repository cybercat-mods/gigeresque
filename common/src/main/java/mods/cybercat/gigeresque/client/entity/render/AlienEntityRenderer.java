package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.AzLayerRenderer;
import mod.azure.azurelib.rewrite.render.AzModelRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererPipeline;
import mods.cybercat.gigeresque.client.entity.render.helper.ClassicModelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.classic.ClassicAlienAnimator;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;

public class AlienEntityRenderer extends AzEntityRenderer<ClassicAlienEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/alien/alien.geo.json");

    public AlienEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<ClassicAlienEntity>builder($ -> MODEL, xeno -> {
                var progress = Math.max(0, Math.min(1 - (xeno.getGrowth() / xeno.getMaxGrowth()), 1));

                if (xeno.isPassedOut()) {
                    return EntityTextures.ALIEN_STATIS;
                }
                if (progress > 0) {
                    return EntityTextures.ALIEN_YOUNG;
                }
                return EntityTextures.ALIEN;
            })
                .setAnimatorProvider(ClassicAlienAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 0.5f;
    }

    @Override
    protected AzEntityRendererPipeline<ClassicAlienEntity> createPipeline(AzEntityRendererConfig<ClassicAlienEntity> config) {
        return new AzEntityRendererPipeline<>(config, this) {

            @Override
            protected AzModelRenderer<ClassicAlienEntity> createModelRenderer(AzLayerRenderer<ClassicAlienEntity> layerRenderer) {
                return new ClassicModelRenderer(this, layerRenderer);
            }
        };
    }

    /*
     * Remove scale once the Rom Alien is implemented
     */
    @Override
    public void render(
        ClassicAlienEntity entity,
        float entityYaw,
        float partialTick,
        PoseStack stack,
        @NotNull MultiBufferSource bufferSource,
        int packedLightIn
    ) {
        var scaleFactor = 0.8f + ((entity.getGrowth() / entity.getMaxGrowth()) / 5f);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.render(entity, entityYaw, partialTick, stack, bufferSource, packedLightIn);
    }
}
