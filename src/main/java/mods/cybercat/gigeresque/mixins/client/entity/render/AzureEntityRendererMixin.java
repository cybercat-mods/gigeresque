package mods.cybercat.gigeresque.mixins.client.entity.render;

import mod.azure.azurelib.common.api.client.model.GeoModel;
import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mod.azure.azurelib.common.api.client.renderer.layer.GeoRenderLayer;
import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mod.azure.azurelib.common.internal.client.renderer.GeoRenderer;
import mods.cybercat.gigeresque.client.entity.render.feature.EggmorphGeoFeatureRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * @author Aelpecyem
 */
@Mixin(value = GeoEntityRenderer.class)
public abstract class AzureEntityRendererMixin<T extends Entity & GeoEntity> {

    @Shadow
    public abstract T getAnimatable();

    @Shadow
    public abstract GeoEntityRenderer<T> addRenderLayer(GeoRenderLayer<T> layer);

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(EntityRendererProvider.Context ctx, GeoModel<T> modelProvider, CallbackInfo ci) {
        if (this.getAnimatable() instanceof Mob)
            this.addRenderLayer(new EggmorphGeoFeatureRenderer<>((GeoRenderer<T>) this));
    }
}
