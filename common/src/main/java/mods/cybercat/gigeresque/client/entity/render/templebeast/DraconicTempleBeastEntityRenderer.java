package mods.cybercat.gigeresque.client.entity.render.templebeast;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.templebeast.DraconicTempleBeastAnimator;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.DraconicTempleBeastEntity;

public class DraconicTempleBeastEntityRenderer extends AzEntityRenderer<DraconicTempleBeastEntity> {

    public DraconicTempleBeastEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<DraconicTempleBeastEntity>builder(
                EntityModels.DRACONICTEMPLEBEAST,
                EntityTextures.DRACONICTEMPLEBEAST
            )
                .setAnimatorProvider(DraconicTempleBeastAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 1.0f;
    }

    @Override
    public void render(
        @NotNull DraconicTempleBeastEntity entity,
        float entityYaw,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource bufferSource,
        int packedLight
    ) {
        poseStack.scale(1.23F, 1.23F, 1.23F);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
