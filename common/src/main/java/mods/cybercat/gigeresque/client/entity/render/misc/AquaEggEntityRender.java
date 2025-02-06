package mods.cybercat.gigeresque.client.entity.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.misc.AquaEggEntity;

public class AquaEggEntityRender extends AzEntityRenderer<AquaEggEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/egg/egg.geo.json");

    public AquaEggEntityRender(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<AquaEggEntity>builder(
                MODEL,
                EntityTextures.AQUA_EGG
            )
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
    }

    @Override
    public void render(
        AquaEggEntity entity,
        float entityYaw,
        float partialTick,
        PoseStack stack,
        @NotNull MultiBufferSource bufferSource,
        int packedLightIn
    ) {
        var scaleFactor = 0.2f + ((entity.getGrowth() / entity.getMaxGrowth()) / 5f);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.render(entity, entityYaw, partialTick, stack, bufferSource, packedLightIn);
    }

}
