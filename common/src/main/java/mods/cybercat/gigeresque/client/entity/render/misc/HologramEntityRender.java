package mods.cybercat.gigeresque.client.entity.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.rewrite.render.layer.AzAutoGlowingLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.misc.HologramAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.misc.HologramAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.misc.HologramEntity;

public class HologramEntityRender extends AzEntityRenderer<HologramEntity> {

    private static final RenderType RENDER_TYPE = RenderType.entityTranslucentCull(EntityTextures.ENGINEER_HOLOGRAM);

    public HologramEntityRender(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<HologramEntity>builder(EntityModels.ENGINEER_HOLOGRAM, EntityTextures.ENGINEER_HOLOGRAM)
                .setAnimatorProvider(HologramAnimator::new)
                .setDeathMaxRotation(0.0F)
                .setRenderType(RENDER_TYPE)
                .addRenderLayer(new AzAutoGlowingLayer<>())
                .build(),
            context
        );
    }

    @Override
    public void render(
        @NotNull HologramEntity entity,
        float entityYaw,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource bufferSource,
        int packedLight
    ) {
        HologramAnimManager.handleAnimations(entity);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
