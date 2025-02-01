package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRendererConfig;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.block.animators.Petrified5Animator;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect5Entity;
import net.minecraft.resources.ResourceLocation;

public class PetrifiedObject5Render extends AzBlockEntityRenderer<PetrifiedOjbect5Entity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/block/neomorph_spore_pods/neomorph_spore_pods.geo.json");

    public PetrifiedObject5Render() {
        super(
                AzBlockEntityRendererConfig.<PetrifiedOjbect5Entity>builder(MODEL, EntityTextures.SPORE_PETRIFIED)
                        .setAnimatorProvider(Petrified5Animator::new)
                        .build()
        );
    }
}
