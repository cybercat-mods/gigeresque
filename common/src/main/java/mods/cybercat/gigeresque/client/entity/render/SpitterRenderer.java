package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.rewrite.render.layer.AzAutoGlowingLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.misc.SpitterAnimator;
import mods.cybercat.gigeresque.common.entity.impl.misc.SpitterEntity;

public class SpitterRenderer extends AzEntityRenderer<SpitterEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/spitter/spitter.geo.json");

    public SpitterRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<SpitterEntity>builder(
                MODEL,
                EntityTextures.SPITTER
            )
                .setAnimatorProvider(SpitterAnimator::new)
                .addRenderLayer(new AzAutoGlowingLayer<>())
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(
        SpitterEntity entity,
        float entityYaw,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource bufferSource,
        int packedLight
    ) {
        var scaleFactor = 0.5f + ((entity.getGrowth() / entity.getMaxGrowth()) / 5f);
        poseStack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
