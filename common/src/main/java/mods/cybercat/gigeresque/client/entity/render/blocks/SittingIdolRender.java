package mods.cybercat.gigeresque.client.entity.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.common.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.common.render.block.AzBlockEntityRendererConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.render.feature.HeldItemLayer;
import mods.cybercat.gigeresque.common.block.animators.SittingIdolAnimator;
import mods.cybercat.gigeresque.common.block.entity.IdolStorageEntity;

public class SittingIdolRender<T extends IdolStorageEntity> extends AzBlockEntityRenderer<T> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/block/sittingidol/sittingidol.geo.json");

    private static final ResourceLocation TEXTURE = Constants.modResource("textures/block/sittingidol/sittingidol.png");

    public SittingIdolRender() {
        super(
            AzBlockEntityRendererConfig.<T>builder(MODEL, TEXTURE)
                .setAnimatorProvider(SittingIdolAnimator::new)
                .addRenderLayer(new HeldItemLayer<>())
                .build()
        );
    }

    @Override
    public void render(
        @NotNull T entity,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource source,
        int packedLight,
        int packedOverlay
    ) {
        poseStack.scale(0.95F, 0.95F, 0.95F);
        super.render(entity, partialTick, poseStack, source, packedLight, packedOverlay);
    }
}
