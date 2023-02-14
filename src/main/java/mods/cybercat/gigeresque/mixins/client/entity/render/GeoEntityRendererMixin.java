package mods.cybercat.gigeresque.mixins.client.entity.render;

/**
 * @author Aelpecyem
 */
//@Environment(EnvType.CLIENT)
//@Mixin(value = GeoEntityRenderer.class, remap = false)
//public abstract class GeoEntityRendererMixin<T extends LivingEntity & IAnimatable> {
//
//	@Shadow
//	public abstract boolean addLayer(GeoLayerRenderer<T> layer);
//
//	@Inject(method = "<init>", at = @At("TAIL"))
//	private void init(EntityRendererProvider.Context ctx, AnimatedGeoModel<T> modelProvider, CallbackInfo ci) {
//		this.addLayer(new EggmorphGeckoFeatureRenderer<>((IGeoRenderer<T>) this));
//	}
//}