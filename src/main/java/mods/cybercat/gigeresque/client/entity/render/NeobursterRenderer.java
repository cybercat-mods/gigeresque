package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.NeobursterModel;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeobursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@Environment(EnvType.CLIENT)
public class NeobursterRenderer extends GeoEntityRenderer<NeobursterEntity> {
	public NeobursterRenderer(EntityRendererProvider.Context context) {
		super(context, new NeobursterModel());
		this.shadowRadius = 0.25f;
	}

	@Override
	public void preRender(PoseStack poseStack, NeobursterEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
		poseStack.scale(0.6F, 0.6F, 0.6F);
	}

	@Override
	protected float getDeathMaxRotation(NeobursterEntity entityLivingBaseIn) {
		return 0.0F;
	}
}
