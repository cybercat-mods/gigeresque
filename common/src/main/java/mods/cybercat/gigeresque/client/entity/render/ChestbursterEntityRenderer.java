package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.animators.classic.ChestbursterAnimator;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;

public class ChestbursterEntityRenderer extends AzEntityRenderer<ChestbursterEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/chestburster/chestburster.geo.json");

    private static final ResourceLocation TEX = Constants.modResource("textures/entity/chestburster/chestburster.png");

    public ChestbursterEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<ChestbursterEntity>builder(MODEL, TEX)
                .setAnimatorProvider(ChestbursterAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 0.1f;
    }

    @Override
    public void render(
        ChestbursterEntity entity,
        float entityYaw,
        float partialTicks,
        PoseStack stack,
        @NotNull MultiBufferSource bufferIn,
        int packedLightIn
    ) {
        float scaleFactor = 1.0f + ((entity.getGrowth() / entity.getMaxGrowth()) / 4.0f);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }
}
