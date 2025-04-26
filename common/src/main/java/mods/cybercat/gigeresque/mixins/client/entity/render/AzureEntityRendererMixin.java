package mods.cybercat.gigeresque.mixins.client.entity.render;

import mod.azure.azurelib.rewrite.render.AzRendererConfig;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.rewrite.render.layer.AzRenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

import mods.cybercat.gigeresque.client.entity.render.feature.EggmorphGeoFeatureRenderer;

@Mixin(value = AzEntityRendererConfig.Builder.class)
public abstract class AzureEntityRendererMixin<T extends Entity> extends AzRendererConfig.Builder<T> {

    protected AzureEntityRendererMixin(
        Function<T, ResourceLocation> modelLocationProvider,
        Function<T, ResourceLocation> textureLocationProvider
    ) {
        super(modelLocationProvider, textureLocationProvider);
    }

    @Inject(
        method = "addRenderLayer(Lmod/azure/azurelib/rewrite/render/layer/AzRenderLayer;)Lmod/azure/azurelib/rewrite/render/entity/AzEntityRendererConfig$Builder;",
        at = @At("RETURN"), remap = false
    )
    private <T extends Entity> void gigeresque$$InjectEggMorph(
        AzRenderLayer<T> renderLayer,
        CallbackInfoReturnable<AzEntityRendererConfig.Builder<T>> cir
    ) {
        AzEntityRendererConfig.Builder<T> originalBuilder = cir.getReturnValue();

        originalBuilder.renderLayers.add(new EggmorphGeoFeatureRenderer<>());

        cir.setReturnValue(originalBuilder);
    }
}
