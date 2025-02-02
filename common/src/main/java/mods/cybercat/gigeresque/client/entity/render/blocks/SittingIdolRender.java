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
                        return bone.getName().equalsIgnoreCase("heldItem") ? new ItemStack(GigBlocks.BEACON_BLOCK.get().asItem()) : null;
                    }

                    @Override
                    protected ItemDisplayContext getTransformTypeForStack(AzBone bone, ItemStack stack) {
                        return ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                    }

                    @Override
                    protected void renderItemForBone(AzRendererPipelineContext<T> context, AzBone bone, ItemStack itemStack) {
                        float rotationAngle = (System.currentTimeMillis() % 3600L) / 10.0F;
                        context.poseStack().mulPose(Axis.XP.rotationDegrees(rotationAngle));
                        context.poseStack().mulPose(Axis.YP.rotationDegrees(rotationAngle));
                        context.poseStack().mulPose(Axis.ZP.rotationDegrees(rotationAngle));
                        super.renderItemForBone(context, bone, itemStack);
                    }
                })
                .build()
        );
    }
}
