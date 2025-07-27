package mods.cybercat.gigeresque.client.entity.render.aqua;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.render.feature.BloodLayer;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.aqua.AquaticChestbursterAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.aqua.AquaticChestbursterAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticChestbursterEntity;

public class AquaticChestbursterEntityRenderer extends AzEntityRenderer<AquaticChestbursterEntity> {

    public AquaticChestbursterEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<AquaticChestbursterEntity>builder(EntityModels.AQUATICBURSTER, EntityTextures.AQUATICBURSTER)
                .setAnimatorProvider(AquaticChestbursterAnimator::new)
                .setDeathMaxRotation(0.0F)
                .addRenderLayer(new BloodLayer<>())
                .setScale(bursterEntity -> 1.0f + (bursterEntity.getGrowth() / bursterEntity.getMaxGrowth()))
                .build(),
            context
        );
        this.shadowRadius = 0.1f;
    }

    @Override
    public void render(
        @NotNull AquaticChestbursterEntity entity,
        float entityYaw,
        float partialTicks,
        @NotNull PoseStack stack,
        @NotNull MultiBufferSource bufferIn,
        int packedLightIn
    ) {
        AquaticChestbursterAnimManager.handleAnimations(entity);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }
}
