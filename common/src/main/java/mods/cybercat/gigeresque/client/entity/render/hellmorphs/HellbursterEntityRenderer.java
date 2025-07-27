package mods.cybercat.gigeresque.client.entity.render.hellmorphs;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mods.cybercat.gigeresque.client.entity.render.feature.BloodLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.hellmorphs.HellbursterAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.hellmorphs.HellbursterAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.HellbursterEntity;

public class HellbursterEntityRenderer extends AzEntityRenderer<HellbursterEntity> {

    public HellbursterEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<HellbursterEntity>builder(EntityModels.HELLBURSTER, EntityTextures.HELLBURSTER)
                .setAnimatorProvider(HellbursterAnimator::new)
                .setDeathMaxRotation(0.0F)
                .addRenderLayer(new BloodLayer<>())
                .build(),
            context
        );
        this.shadowRadius = 0.3f;
    }

    @Override
    public void render(
        @NotNull HellbursterEntity entity,
        float entityYaw,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource bufferSource,
        int packedLight
    ) {
        float scaleFactor = 1.0f + (entity.getGrowth() / entity.getMaxGrowth());
        poseStack.scale(scaleFactor, scaleFactor, scaleFactor);
        HellbursterAnimManager.handleAnimations(entity);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
