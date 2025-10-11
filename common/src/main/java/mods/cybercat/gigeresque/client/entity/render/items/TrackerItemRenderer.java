package mods.cybercat.gigeresque.client.entity.render.items;

import mod.azure.azurelib.common.render.item.AzItemRenderer;
import mod.azure.azurelib.common.render.item.AzItemRendererConfig;
import mod.azure.azurelib.common.render.layer.AzAutoGlowingLayer;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.item.animator.TrackerAnimator;

public class TrackerItemRenderer extends AzItemRenderer {

    private static final ResourceLocation MODEL = Constants.modResource("geo/item/tracker.geo.json");

    private static final ResourceLocation TEXTURE = Constants.modResource("textures/item/tracker.png");

    public TrackerItemRenderer() {
        super(
            AzItemRendererConfig.builder(itemStack -> MODEL, itemStack -> TEXTURE)
                .addRenderLayer(new AzAutoGlowingLayer<>())
                .setAnimatorProvider(TrackerAnimator::new)
                .useNewOffset(true)
                .build()
        );
    }
}
