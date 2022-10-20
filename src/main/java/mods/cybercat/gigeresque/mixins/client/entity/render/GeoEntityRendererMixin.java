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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

/**
 * @author Aelpecyem
 */
@Environment(EnvType.CLIENT)
@Mixin(value = GeoEntityRenderer.class, remap = false)
public abstract class GeoEntityRendererMixin<T extends LivingEntity & IAnimatable> {
	@Shadow
	public abstract boolean addLayer(GeoLayerRenderer<T> layer);

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(EntityRendererProvider.Context ctx, AnimatedGeoModel<T> modelProvider, CallbackInfo ci) {
		this.addLayer(new EggmorphGeoFeatureRenderer<>((IGeoRenderer<T>) this));
	}
}
