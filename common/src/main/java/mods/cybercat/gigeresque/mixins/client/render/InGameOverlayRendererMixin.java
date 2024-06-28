package mods.cybercat.gigeresque.mixins.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
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
@Mixin(ScreenEffectRenderer.class)
public class InGameOverlayRendererMixin {

    private static int fovEggticker = 0;
    private static int fovGooticker = 0;

    @Inject(method = {"renderScreenEffect"}, at = {@At("RETURN")})
    private static void renderOverlays(Minecraft client, PoseStack matrices, CallbackInfo ci) {
        assert client.player != null;
        if (!client.player.isSpectator()) {
            var d = client.player.getEyeY() - 0.1111111119389534D;
            var blockPos = BlockPos.containing(client.player.getX(), d, client.player.getZ());
            var fluidState = client.player.level().getFluidState(blockPos);
            if (fluidState.is(GigFluids.BLACK_FLUID_STILL.get()) || fluidState.is(GigFluids.BLACK_FLUID_FLOWING.get()))
                renderOverlay(client, matrices, 1, EntityTextures.BLACK_FLUID_TEXTURE);

            if (Constants.isNotCreativeSpecPlayer.test(client.player) && client.player.hasEffect(
                    GigStatusEffects.DNA)) {
                fovGooticker++;
                var dnaDuration = Math.max(0, Math.min(fovGooticker / CommonMod.config.getgooEffectTickTimer(), 1));
                renderOverlay(client, matrices, dnaDuration, EntityTextures.BLACK_FLUID_TEXTURE);
            }

            if (Constants.isNotCreativeSpecPlayer.test(
                    client.player) && client.player.hasEffect(GigStatusEffects.EGGMORPHING)) {
                fovEggticker++;
                var eggmorphingProgress = Math.max(0,
                        Math.min(fovEggticker / CommonMod.config.getEggmorphTickTimer(), 1));
                renderOverlay(client, matrices, eggmorphingProgress, EntityTextures.EGGMORPH_OVERLAY_TEXTURE);
            }
        }
    }

    private static void renderOverlay(Minecraft client, PoseStack matrices, float progress, ResourceLocation resourceLocation) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, resourceLocation);
        BlockPos blockpos = BlockPos.containing(client.player.getX(), client.player.getEyeY(), client.player.getZ());
        float f = LightTexture.getBrightness(client.player.level().dimensionType(), client.player.level().getMaxLocalRawBrightness(blockpos));
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(f, f, f, progress);
        float f7 = -client.player.getYRot() / 64.0F;
        float f8 = client.player.getXRot() / 64.0F;
        Matrix4f matrix4f = matrices.last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.addVertex(matrix4f, -1.0F, -1.0F, -0.5F).setUv(4.0F + f7, 4.0F + f8);
        bufferbuilder.addVertex(matrix4f, 1.0F, -1.0F, -0.5F).setUv(0.0F + f7, 4.0F + f8);
        bufferbuilder.addVertex(matrix4f, 1.0F, 1.0F, -0.5F).setUv(0.0F + f7, 0.0F + f8);
        bufferbuilder.addVertex(matrix4f, -1.0F, 1.0F, -0.5F).setUv(4.0F + f7, 0.0F + f8);
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }
}
