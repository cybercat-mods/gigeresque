package mods.cybercat.gigeresque.client.entity.render.hellmorphs;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.animators.hellmorphs.HellbursterAnimator;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.HellbursterEntity;

public class HellbursterEntityRenderer extends AzEntityRenderer<HellbursterEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/hell_burster/hell_burster.geo.json");

    private static final ResourceLocation TEX = Constants.modResource("textures/entity/hell_burster/hell_burster.png");

    public HellbursterEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<HellbursterEntity>builder(MODEL, TEX)
                .setAnimatorProvider(HellbursterAnimator::new)
                .setDeathMaxRotation(0.0F)
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
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
