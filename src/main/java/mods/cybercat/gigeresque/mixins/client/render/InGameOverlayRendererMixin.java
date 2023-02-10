package mods.cybercat.gigeresque.mixins.client.render;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.core.BlockPos;

/**
 * @author Boston Vanseghi
 */
@Environment(EnvType.CLIENT)
@Mixin(ScreenEffectRenderer.class)
public class InGameOverlayRendererMixin {

	@Inject(method = { "renderScreenEffect" }, at = { @At("RETURN") })
	private static void renderOverlays(Minecraft client, PoseStack matrices, CallbackInfo ci) {
		if (!client.player.isSpectator()) {
			var d = client.player.getEyeY() - 0.1111111119389534D;
			var blockPos = new BlockPos(client.player.getX(), d, client.player.getZ());
			var fluidState = client.player.level.getFluidState(blockPos);

			if (fluidState.createLegacyBlock().getBlock() == GIgBlocks.BLACK_FLUID)
				renderBlackFluidOverlay(client, matrices);

			if (!client.player.isCreative() && client.player instanceof Eggmorphable eggmorphable
					&& eggmorphable.isEggmorphing()) {
				float eggmorphingProgress = (GigeresqueConfig.getEggmorphTickTimer()
						- eggmorphable.getTicksUntilEggmorphed()) / GigeresqueConfig.getEggmorphTickTimer();
				renderEggmorphOverlay(client, matrices, 1 - eggmorphingProgress);
			}
		}
	}

	private static void renderBlackFluidOverlay(Minecraft client, PoseStack matrices) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.enableTexture();
		RenderSystem.setShaderTexture(0, EntityTextures.BLACK_FLUID_TEXTURE);
		BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
		var f = client.player.getLightLevelDependentMagicValue();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShaderColor(f, f, f, 1.0F);
		var m = -client.player.getYRot() / 64.0F;
		var n = client.player.getXRot() / 64.0F;
		Matrix4f matrix4f = matrices.last().pose();
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).uv(4.0F + m, 4.0F + n).endVertex();
		bufferBuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).uv(0.0F + m, 4.0F + n).endVertex();
		bufferBuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).uv(0.0F + m, 0.0F + n).endVertex();
		bufferBuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).uv(4.0F + m, 0.0F + n).endVertex();
		BufferUploader.drawWithShader(bufferBuilder.end());
		RenderSystem.disableBlend();
	}

	private static void renderEggmorphOverlay(Minecraft client, PoseStack matrices, float progress) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.enableTexture();
		RenderSystem.setShaderTexture(0, EntityTextures.EGGMORPH_OVERLAY_TEXTURE);
		BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
		var f = client.player.getLightLevelDependentMagicValue();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShaderColor(f, f, f, progress);
		var m = -client.player.getYRot() / 64.0F;
		var n = client.player.getXRot() / 64.0F;
		Matrix4f matrix4f = matrices.last().pose();
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).uv(4.0F + m, 4.0F + n).endVertex();
		bufferBuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).uv(0.0F + m, 4.0F + n).endVertex();
		bufferBuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).uv(0.0F + m, 0.0F + n).endVertex();
		bufferBuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).uv(4.0F + m, 0.0F + n).endVertex();
		BufferUploader.drawWithShader(bufferBuilder.end());
		RenderSystem.disableBlend();
	}
}
