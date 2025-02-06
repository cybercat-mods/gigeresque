package mods.cybercat.gigeresque.client.entity.render.neo;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.animators.neo.NeobursterAnimator;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeobursterEntity;

public class NeobursterRenderer extends AzEntityRenderer<NeobursterEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/neoburster/neoburster.geo.json");

    private static final ResourceLocation TEX = Constants.modResource("textures/entity/neoburster/neoburster.png");

    public NeobursterRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<NeobursterEntity>builder(MODEL, TEX)
                .setAnimatorProvider(NeobursterAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 0.25f;
    }

    @Override
    public void render(
        @NotNull NeobursterEntity entity,
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
