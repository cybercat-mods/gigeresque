package mods.cybercat.gigeresque.client.entity.render.items;

import mod.azure.azurelib.common.render.item.AzItemRenderer;
import mod.azure.azurelib.common.render.item.AzItemRendererConfig;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;

public class SporeItemBlockRender extends AzItemRenderer {

    private static final ResourceLocation MODEL = Constants.modResource("geo/item/neomorph_spore_pods/neomorph_spore_pods.geo.json");

    private static final ResourceLocation TEXTURE = Constants.modResource("textures/block/neomorph_spore_pods/neomorph_spore_pods.png");

    public SporeItemBlockRender() {
        super(
            AzItemRendererConfig.builder(MODEL, TEXTURE)
                .build()
        );
    }

}
