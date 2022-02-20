package com.bvanseg.gigeresque.mixins.client.entity.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bvanseg.gigeresque.client.entity.render.feature.EggmorphFeatureRenderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

/**
 * @author Aelpecyem
 */
@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
	@Shadow
	protected abstract boolean addFeature(FeatureRenderer<T, M> feature);

	@SuppressWarnings("unchecked")
	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(EntityRendererFactory.Context ctx, M model, float shadowRadius, CallbackInfo ci) {
		this.addFeature(new EggmorphFeatureRenderer<>((FeatureRendererContext<T, M>) this));
	}
}
