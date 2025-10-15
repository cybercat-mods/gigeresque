package mods.cybercat.gigeresque.mixins.client.entity.render;

import mod.azure.azurelib.common.render.AzRendererConfig;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.common.render.layer.AzRenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;
import java.util.function.Function;

import mods.cybercat.gigeresque.client.entity.render.feature.EggmorphAzLayer;

@Mixin(value = AzEntityRendererConfig.Builder.class)
public abstract class AzureEntityRendererMixin<T extends Entity> extends AzRendererConfig.Builder<UUID, T> {

    protected AzureEntityRendererMixin(
        Function<T, ResourceLocation> modelLocationProvider,
        Function<T, ResourceLocation> textureLocationProvider
    ) {
        super(modelLocationProvider, textureLocationProvider);
    }

    @Inject(
        method = "addRenderLayer(Lmod/azure/azurelib/common/render/layer/AzRenderLayer;)Lmod/azure/azurelib/common/render/entity/AzEntityRendererConfig$Builder;",
        at = @At("RETURN"), remap = false
    )
    private <T extends Entity> void gigeresque$$InjectEggMorph(
        AzRenderLayer<UUID, T> renderLayer,
        CallbackInfoReturnable<AzEntityRendererConfig.Builder<T>> cir
    ) {
        AzEntityRendererConfig.Builder<T> originalBuilder = cir.getReturnValue();

        originalBuilder.renderLayers.add(new EggmorphAzLayer<>());

        cir.setReturnValue(originalBuilder);
    }
}
