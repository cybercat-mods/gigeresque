package mods.cybercat.gigeresque.mixins.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;

/**
 * @author Boston Vanseghi
 */
@Mixin(ScreenEffectRenderer.class)
public class InGameOverlayRendererMixin {

    private static int fovEggticker = 0;

    private static int fovGooticker = 0;

    @Inject(method = { "renderScreenEffect" }, at = { @At("RETURN") })
    private static void gigeresque$renderOverlays(Minecraft client, PoseStack matrices, CallbackInfo ci) {
        assert client.player != null;
        if (!client.player.isSpectator()) {
            var d = client.player.getEyeY() - 0.1111111119389534D;
            var blockPos = BlockPos.containing(client.player.getX(), d, client.player.getZ());
            var fluidState = client.player.level().getFluidState(blockPos);
            if (fluidState.is(GigFluids.BLACK_FLUID_STILL.get()) || fluidState.is(GigFluids.BLACK_FLUID_FLOWING.get()))
                gigeresque$renderOverlay(client, matrices, 1, EntityTextures.BLACK_FLUID_TEXTURE);

            if (
                Constants.isNotCreativeSpecPlayer.test(client.player) && client.player.hasEffect(
                    GigStatusEffects.DNA
                )
            ) {
                fovGooticker++;
                var dnaDuration = Math.max(0, Math.min(fovGooticker / CommonMod.config.getgooEffectTickTimer(), 1));
                gigeresque$renderOverlay(client, matrices, dnaDuration, EntityTextures.BLACK_FLUID_TEXTURE);
            } else {
                fovGooticker = 0;
            }

            if (
                Constants.isNotCreativeSpecPlayer.test(
                    client.player
                ) && client.player.hasEffect(GigStatusEffects.EGGMORPHING)
            ) {
                fovEggticker++;
                var eggmorphingProgress = Math.clamp(fovEggticker / CommonMod.config.getEggmorphTickTimer(), 0, 1);
                gigeresque$renderOverlay(client, matrices, eggmorphingProgress, EntityTextures.EGGMORPH_OVERLAY_TEXTURE);
            } else {
                fovEggticker = 0;
            }
        }
    }

    @Unique
    private static void gigeresque$renderOverlay(
        Minecraft minecraft,
        PoseStack poseStack,
        float progress,
        ResourceLocation resourceLocation
    ) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, resourceLocation);
        BlockPos blockpos = BlockPos.containing(minecraft.player.getX(), minecraft.player.getEyeY(), minecraft.player.getZ());
        float f = LightTexture.getBrightness(
            minecraft.player.level().dimensionType(),
            minecraft.player.level().getMaxLocalRawBrightness(blockpos)
        );
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(f, f, f, progress);
        float f1 = 4.0F;
        float f2 = -1.0F;
        float f3 = 1.0F;
        float f4 = -1.0F;
        float f5 = 1.0F;
        float f6 = -0.5F;
        float f7 = -minecraft.player.getYRot() / 64.0F;
        float f8 = minecraft.player.getXRot() / 64.0F;
        Matrix4f matrix4f = poseStack.last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.addVertex(matrix4f, -1.0F, -1.0F, -0.5F).setUv(4.0F + f7, 4.0F + f8);
        bufferbuilder.addVertex(matrix4f, 1.0F, -1.0F, -0.5F).setUv(0.0F + f7, 4.0F + f8);
        bufferbuilder.addVertex(matrix4f, 1.0F, 1.0F, -0.5F).setUv(0.0F + f7, 0.0F + f8);
        bufferbuilder.addVertex(matrix4f, -1.0F, 1.0F, -0.5F).setUv(4.0F + f7, 0.0F + f8);
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, progress);
        RenderSystem.disableBlend();
    }
}
