package mods.cybercat.gigeresque.mixins.client.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import mods.cybercat.gigeresque.client.GigeresqueClient;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingRenderer <T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {

    protected MixinLivingRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    private void livingRenderPre(T livingEntity, float f, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
    	GigeresqueClient.onPreRenderLiving(livingEntity, partialTicks, poseStack);
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("RETURN"))
    private void livingRenderPost(T livingEntity, float f, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
    	GigeresqueClient.onPostRenderLiving(livingEntity, partialTicks, poseStack, multiBufferSource);
    }
}