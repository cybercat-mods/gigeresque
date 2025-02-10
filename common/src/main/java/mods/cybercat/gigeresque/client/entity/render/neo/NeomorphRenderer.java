package mods.cybercat.gigeresque.client.entity.render.neo;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.AzLayerRenderer;
import mod.azure.azurelib.rewrite.render.AzModelRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererPipeline;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.model.NeomorphModelRenderer;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.neo.NeomorphAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.neo.NeomorphAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphEntity;

public class NeomorphRenderer extends AzEntityRenderer<NeomorphEntity> {

    public NeomorphRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<NeomorphEntity>builder(
                EntityModels.NEOMORPH,
                EntityTextures.NEOMORPH
            )
                .setAnimatorProvider(NeomorphAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 0.5f;
    }

    @Override
    protected AzEntityRendererPipeline<NeomorphEntity> createPipeline(AzEntityRendererConfig<NeomorphEntity> config) {
        return new AzEntityRendererPipeline<>(config, this) {

            @Override
            protected AzModelRenderer<NeomorphEntity> createModelRenderer(AzLayerRenderer<NeomorphEntity> layerRenderer) {
                return new NeomorphModelRenderer(this, layerRenderer);
            }
        };
    }

    @Override
    public void render(
        @NotNull NeomorphEntity entity,
        float entityYaw,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource bufferSource,
        int packedLight
    ) {
        poseStack.scale(0.76F, 0.76F, 0.76F);
        NeomorphAnimManager.handleAnimations(entity);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
