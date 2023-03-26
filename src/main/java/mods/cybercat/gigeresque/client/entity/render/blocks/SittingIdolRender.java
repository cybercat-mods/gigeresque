package mods.cybercat.gigeresque.client.entity.render.blocks;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.renderer.GeoBlockRenderer;
import mod.azure.azurelib.renderer.layer.BlockAndItemGeoLayer;
import mods.cybercat.gigeresque.client.entity.model.blocks.SittingIdolModel;
import mods.cybercat.gigeresque.common.block.entity.IdolStorageEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SittingIdolRender extends GeoBlockRenderer<IdolStorageEntity> {

	public SittingIdolRender() {
		super(new SittingIdolModel());
		this.addRenderLayer(new BlockAndItemGeoLayer<>(this) {
			@Nullable
			@Override
			protected ItemStack getStackForBone(GeoBone bone, IdolStorageEntity animatable) {
				return switch (bone.getName()) {
				case "heldItem" -> new ItemStack(Items.NETHERITE_SCRAP);
				default -> null;
				};
			}

			@Override
			protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, IdolStorageEntity animatable) {
				return switch (bone.getName()) {
				default -> ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
				};
			}

			@Override
			protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, IdolStorageEntity animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
				poseStack.mulPose(Axis.XP.rotationDegrees(0));
				poseStack.mulPose(Axis.YP.rotationDegrees(0));
				poseStack.mulPose(Axis.ZP.rotationDegrees(0));
				super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
			}
		});
	}

}