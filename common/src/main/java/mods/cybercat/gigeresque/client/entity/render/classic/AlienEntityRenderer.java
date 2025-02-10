package mods.cybercat.gigeresque.client.entity.render.classic;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.AzLayerRenderer;
import mod.azure.azurelib.rewrite.render.AzModelRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererPipeline;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.ClassicModelRenderer;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.classic.ClassicAlienAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.classic.ClassicAlienAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;

public class AlienEntityRenderer extends AzEntityRenderer<ClassicAlienEntity> {

    public AlienEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<ClassicAlienEntity>builder(EntityModels.ALIEN, EntityTextures.ALIEN)
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
        if (scaleFactor < 1.0F)
            stack.scale(scaleFactor, scaleFactor, scaleFactor);
        ClassicAlienAnimManager.handleAnimations(entity);
        super.render(entity, entityYaw, partialTick, stack, bufferSource, packedLightIn);
    }
}
