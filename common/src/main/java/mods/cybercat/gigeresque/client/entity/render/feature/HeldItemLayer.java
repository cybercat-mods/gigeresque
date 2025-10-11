package mods.cybercat.gigeresque.client.entity.render.feature;

import com.mojang.math.Axis;
import mod.azure.azurelib.common.model.AzBone;
import mod.azure.azurelib.common.render.AzRendererPipelineContext;
import mod.azure.azurelib.common.render.layer.AzBlockAndItemLayer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import mods.cybercat.gigeresque.common.block.GigBlocks;

public class HeldItemLayer<K, T> extends AzBlockAndItemLayer<K, T> {

    @Override
    public ItemStack itemStackForBone(AzBone bone, T animatable) {
        return bone.getName().equalsIgnoreCase("heldItem") ? new ItemStack(GigBlocks.BEACON_BLOCK.get().asItem()) : null;
    }

    @Override
    protected ItemDisplayContext getTransformTypeForStack(AzBone bone, ItemStack stack, T animatable) {
        return ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
    }

    @Override
    protected void renderItemForBone(
        AzRendererPipelineContext<K, T> context,
        AzBone bone,
        ItemStack itemStack,
        T animatable
    ) {
        float rotationAngle = (System.currentTimeMillis() % 3600L) / 10.0F;
        context.poseStack().mulPose(Axis.XP.rotationDegrees(rotationAngle));
        context.poseStack().mulPose(Axis.YP.rotationDegrees(rotationAngle));
        context.poseStack().mulPose(Axis.ZP.rotationDegrees(rotationAngle));
        super.renderItemForBone(context, bone, itemStack, animatable);
    }
}
