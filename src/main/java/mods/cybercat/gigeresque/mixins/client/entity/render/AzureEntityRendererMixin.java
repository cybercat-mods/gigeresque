package mods.cybercat.gigeresque.mixins.client.entity.render;

import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.renderer.layer.GeoRenderLayer;
import mods.cybercat.gigeresque.client.entity.render.feature.EggmorphGeoFeatureRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Aelpecyem
 */
@Environment(EnvType.CLIENT)
@Mixin(value = GeoEntityRenderer.class, remap = false)
public abstract class AzureEntityRendererMixin<T extends Entity & GeoEntity> {

    @Shadow
    public abstract T getAnimatable();

    @Shadow
    public abstract GeoEntityRenderer<T> addRenderLayer(GeoRenderLayer<T> layer);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityRendererProvider.Context ctx, GeoModel<T> modelProvider, CallbackInfo ci) {
        if (this.getAnimatable() instanceof Mob)
            this.addRenderLayer(new EggmorphGeoFeatureRenderer<>((GeoRenderer<T>) this));
    }
}
