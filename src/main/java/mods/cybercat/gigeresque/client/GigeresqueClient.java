package mods.cybercat.gigeresque.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import mods.cybercat.gigeresque.client.block.BlockRenderLayers;
import mods.cybercat.gigeresque.client.entity.render.EntityRenderers;
import mods.cybercat.gigeresque.client.fluid.render.FluidRenderHandlers;
import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.interfacing.IClimberEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class GigeresqueClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		new BlockRenderLayers().initialize();
		new FluidRenderHandlers().initialize();
		new EntityRenderers().initialize();
		new Particles().initialize();
	}

	public static void onPreRenderLiving(LivingEntity entity, float partialTicks, PoseStack matrixStack) {
		if (entity instanceof IClimberEntity climber) {
			var orientation = climber.getOrientation();
			var renderOrientation = climber.calculateOrientation(partialTicks);
			climber.setRenderOrientation(renderOrientation);

			var verticalOffset = climber.getVerticalOffset(partialTicks);

			var x = climber.getAttachmentOffset(Direction.Axis.X, partialTicks) - (float) renderOrientation.normal().x * verticalOffset;
			var y = climber.getAttachmentOffset(Direction.Axis.Y, partialTicks) - (float) renderOrientation.normal().y * verticalOffset;
			var z = climber.getAttachmentOffset(Direction.Axis.Z, partialTicks) - (float) renderOrientation.normal().z * verticalOffset;

			matrixStack.translate(x, y, z);

			matrixStack.mulPose(Axis.YP.rotationDegrees(renderOrientation.yaw()));
			matrixStack.mulPose(Axis.XP.rotationDegrees(renderOrientation.pitch()));
			matrixStack.mulPose(Axis.YP.rotationDegrees((float) Math.signum(0.5f - orientation.componentY() - orientation.componentZ() - orientation.componentX()) * renderOrientation.yaw()));
		}
	}

	public static void onPostRenderLiving(LivingEntity entity, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn) {
		if (entity instanceof IClimberEntity climber) {
			var orientation = climber.getOrientation();
			var renderOrientation = climber.getRenderOrientation();

			if (renderOrientation != null) {
				var verticalOffset = climber.getVerticalOffset(partialTicks);

				var x = climber.getAttachmentOffset(Direction.Axis.X, partialTicks) - (float) renderOrientation.normal().x * verticalOffset;
				var y = climber.getAttachmentOffset(Direction.Axis.Y, partialTicks) - (float) renderOrientation.normal().y * verticalOffset;
				var z = climber.getAttachmentOffset(Direction.Axis.Z, partialTicks) - (float) renderOrientation.normal().z * verticalOffset;

				matrixStack.mulPose(Axis.YP.rotationDegrees(-(float) Math.signum(0.5f - orientation.componentY() - orientation.componentZ() - orientation.componentX()) * renderOrientation.yaw()));
				matrixStack.mulPose(Axis.XP.rotationDegrees(-renderOrientation.pitch()));
				matrixStack.mulPose(Axis.YP.rotationDegrees(-renderOrientation.yaw()));

				matrixStack.translate(-x, -y, -z);
			}
		}
	}
}
