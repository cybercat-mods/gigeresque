package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.neo.NeomorphAdolescentAnimator;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphAdolescentEntity;

public class NeomorphAdolescentRenderer extends AzEntityRenderer<NeomorphAdolescentEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/neomorph_adolescent/neomorph_adolescent.geo.json");

    public NeomorphAdolescentRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<NeomorphAdolescentEntity>builder(
                MODEL,
                EntityTextures.NEOMORPH_ADOLESCENT
            )
                .setAnimatorProvider(NeomorphAdolescentAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 0.5f;
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
        var scaleFactor = 1.0f + ((entity.getGrowth() / entity.getMaxGrowth()) / 5f);
        poseStack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
