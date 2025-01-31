package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.animators.aqua.AquaticChestbursterAnimator;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticChestbursterEntity;

public class AquaticChestbursterEntityRenderer extends AzEntityRenderer<AquaticChestbursterEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/aquatic_chestburster/aquatic_chestburster.geo.json");

    private static final ResourceLocation TEX = Constants.modResource("textures/entity/aquatic_chestburster/aquatic_chestburster.png");

    public AquaticChestbursterEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<AquaticChestbursterEntity>builder(MODEL, TEX)
                .setAnimatorProvider(AquaticChestbursterAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 0.1f;
    }

    @Override
    public void render(
        AquaticChestbursterEntity entity,
        float entityYaw,
        float partialTicks,
        PoseStack stack,
        @NotNull MultiBufferSource bufferIn,
        int packedLightIn
    ) {
        float scaleFactor = 1.0f + (entity.getGrowth() / entity.getMaxGrowth());
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }
}
