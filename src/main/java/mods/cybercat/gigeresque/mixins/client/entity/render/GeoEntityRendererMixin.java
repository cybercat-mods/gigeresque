package mods.cybercat.gigeresque.mixins.client.entity.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mods.cybercat.gigeresque.client.entity.render.feature.EggmorphGeoFeatureRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

/**
 * @author Aelpecyem
 */
@Environment(EnvType.CLIENT)
@Mixin(value = GeoEntityRenderer.class, remap = false)
public abstract class GeoEntityRendererMixin<T extends LivingEntity & GeoAnimatable> {
	@Shadow
	public abstract GeoEntityRenderer<T> addRenderLayer(GeoRenderLayer<T> layer);

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(EntityRendererProvider.Context ctx, GeoModel<T> modelProvider, CallbackInfo ci) {
		this.addRenderLayer(new EggmorphGeoFeatureRenderer<>((GeoRenderer<T>) this));
	}
}
