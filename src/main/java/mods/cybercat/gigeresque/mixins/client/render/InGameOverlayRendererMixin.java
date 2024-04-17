package mods.cybercat.gigeresque.mixins.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Boston Vanseghi
 */
@Environment(EnvType.CLIENT)
@Mixin(ScreenEffectRenderer.class)
public class InGameOverlayRendererMixin {

    @Inject(method = {"renderScreenEffect"}, at = {@At("RETURN")})
    private static void renderOverlays(Minecraft client, PoseStack matrices, CallbackInfo ci) {
        assert client.player != null;
        if (!client.player.isSpectator()) {
            var d = client.player.getEyeY() - 0.1111111119389534D;
            var blockPos = BlockPos.containing(client.player.getX(), d, client.player.getZ());
            var fluidState = client.player.level().getFluidState(blockPos);

            if (fluidState.createLegacyBlock().getBlock() == GigBlocks.BLACK_FLUID)
                renderOverlay(client, matrices, 1, EntityTextures.BLACK_FLUID_TEXTURE);

            if (Constants.isNotCreativeSpecPlayer.test(client.player) && client.player.hasEffect(
                    GigStatusEffects.DNA)) {
                var dnaDuration = (float) (Gigeresque.config.getgooEffectTickTimer() - client.player.getEffect(
                        GigStatusEffects.DNA).getDuration()) / Gigeresque.config.getgooEffectTickTimer();
                renderOverlay(client, matrices, 0 + dnaDuration, EntityTextures.BLACK_FLUID_TEXTURE);
            }

            if (Constants.isNotCreativeSpecPlayer.test(
                    client.player) && client.player instanceof Eggmorphable eggmorphable && eggmorphable.isEggmorphing()) {
                var eggmorphingProgress = (Gigeresque.config.getEggmorphTickTimer() - eggmorphable.getTicksUntilEggmorphed()) / Gigeresque.config.getEggmorphTickTimer();
                renderOverlay(client, matrices, 1 - eggmorphingProgress, EntityTextures.EGGMORPH_OVERLAY_TEXTURE);
            }
        }
    }

    private static void renderOverlay(Minecraft client, PoseStack matrices, float progress, ResourceLocation resourceLocation) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, resourceLocation);
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        assert client.player != null;
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
