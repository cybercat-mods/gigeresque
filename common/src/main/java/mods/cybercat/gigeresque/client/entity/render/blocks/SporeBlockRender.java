package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRendererConfig;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.animators.SporeAnimator;
import mods.cybercat.gigeresque.common.block.entity.SporeBlockEntity;
import net.minecraft.resources.ResourceLocation;

public class SporeBlockRender extends AzBlockEntityRenderer<SporeBlockEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/block/neomorph_spore_pods/neomorph_spore_pods.geo.json");

    private static final ResourceLocation TEXTURE = Constants.modResource("textures/block/neomorph_spore_pods/neomorph_spore_pods.png");

    public SporeBlockRender() {
        super(
                AzBlockEntityRendererConfig.<SporeBlockEntity>builder(MODEL, TEXTURE)
                        .setAnimatorProvider(SporeAnimator::new)
                        .build()
        );
    }

}
