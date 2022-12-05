package mods.cybercat.gigeresque.client.entity.render.blocks;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import mods.cybercat.gigeresque.client.entity.model.blocks.SittingIdolModel;
import mods.cybercat.gigeresque.common.block.entity.IdolStorageEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

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
			protected ItemTransforms.TransformType getTransformTypeForStack(GeoBone bone, ItemStack stack,
					IdolStorageEntity animatable) {
				return switch (bone.getName()) {
				default -> ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
				};
			}

			@Override
			protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack,
					IdolStorageEntity animatable, MultiBufferSource bufferSource, float partialTick, int packedLight,
					int packedOverlay) {
				poseStack.mulPose(Axis.XP.rotationDegrees(0));
				poseStack.mulPose(Axis.YP.rotationDegrees(0));
				poseStack.mulPose(Axis.ZP.rotationDegrees(0));
				super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight,
						packedOverlay);
			}
		});
	}

}