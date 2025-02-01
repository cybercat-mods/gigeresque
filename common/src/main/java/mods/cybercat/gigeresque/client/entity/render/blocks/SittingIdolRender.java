package mods.cybercat.gigeresque.client.entity.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.azure.azurelib.rewrite.model.AzBone;
import mod.azure.azurelib.rewrite.render.AzRendererPipelineContext;
import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRendererConfig;
import mod.azure.azurelib.rewrite.render.layer.AzBlockAndItemLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.block.animators.SittingIdolAnimator;
import mods.cybercat.gigeresque.common.block.entity.IdolStorageEntity;

public class SittingIdolRender<T extends IdolStorageEntity> extends AzBlockEntityRenderer<T> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/block/sittingidol/sittingidol.geo.json");

    private static final ResourceLocation TEXTURE = Constants.modResource("textures/block/sittingidol/sittingidol.png");

    public SittingIdolRender() {
        super(
            AzBlockEntityRendererConfig.<T>builder(MODEL, TEXTURE)
                .setAnimatorProvider(SittingIdolAnimator::new)
                .addRenderLayer(new AzBlockAndItemLayer<T>() {

                    @Override
                    public ItemStack itemStackForBone(AzBone bone) {
                        return bone.getName().equalsIgnoreCase("heldItem") ? new ItemStack(Items.AIR) : null;
                    }

                    @Override
                    protected ItemDisplayContext getTransformTypeForStack(AzBone bone, ItemStack stack) {
                        return ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                    }

                    @Override
                    protected void renderItemForBone(AzRendererPipelineContext<T> context, AzBone bone, ItemStack itemStack) {
                        context.poseStack().mulPose(Axis.XP.rotationDegrees(0));
                        context.poseStack().mulPose(Axis.YP.rotationDegrees(0));
                        context.poseStack().mulPose(Axis.ZP.rotationDegrees(0));
                        super.renderItemForBone(context, bone, itemStack);
                    }
                })
                .build()
        );
    }

    @Override
    public void render(
        T animatable,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource bufferSource,
        int packedLight,
        int packedOverlay
    ) {
        BlockPos entityPos = animatable.getBlockPos();
        int searchRadius = 2;

        BlockPos targetBlockPos = null;
        for (int x = -searchRadius; x <= searchRadius; x++) {
            for (int y = -searchRadius; y <= searchRadius; y++) {
                for (int z = -searchRadius; z <= searchRadius; z++) {
                    BlockPos checkPos = entityPos.offset(x, y, z);
                    var blockState = animatable.getLevel().getBlockState(checkPos);
                    if (blockState.is(GigBlocks.ALIEN_STORAGE_BLOCK_INVIS2.get())) {
                        targetBlockPos = checkPos;
                        break;
                    }
                }
                if (targetBlockPos != null)
                    break;
            }
            if (targetBlockPos != null)
                break;
        }
        if (targetBlockPos != null) {
            double dx = targetBlockPos.getX() - entityPos.getX();
            double dz = targetBlockPos.getZ() - entityPos.getZ();
            float yaw = (float) (Math.atan2(dz, dx) * (180 / Math.PI)) - 90.0F;
            poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        }
        super.render(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }
}
