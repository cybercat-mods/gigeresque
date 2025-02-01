package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.AzLayerRenderer;
import mod.azure.azurelib.rewrite.render.AzModelRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererPipeline;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.render.helper.NeomorphModelRenderer;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.neo.NeomorphAnimator;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphEntity;

public class NeomorphRenderer extends AzEntityRenderer<NeomorphEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/neomorph/neomorph.geo.json");

    public NeomorphRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<NeomorphEntity>builder(
                MODEL,
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
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
