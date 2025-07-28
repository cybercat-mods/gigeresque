package mods.cybercat.gigeresque.client.entity.render.neo;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.neo.NeomorphAdolescentAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.neo.NeomorphAdolescentAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphAdolescentEntity;

public class NeomorphAdolescentRenderer extends AzEntityRenderer<NeomorphAdolescentEntity> {

    public NeomorphAdolescentRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<NeomorphAdolescentEntity>builder(
                EntityModels.NEOMORPH_ADOLESCENT,
                EntityTextures.NEOMORPH_ADOLESCENT
            )
                .setAnimatorProvider(NeomorphAdolescentAnimator::new)
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.5F)
                .setScale(neoEntity -> 1.0f + (neoEntity.getGrowth() / neoEntity.getMaxGrowth()) / 5f)
                .build(),
            context
        );
    }

    @Override
    public void render(
        @NotNull NeomorphAdolescentEntity entity,
        float entityYaw,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource bufferSource,
        int packedLight
    ) {
        NeomorphAdolescentAnimManager.handleAnimations(entity);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
