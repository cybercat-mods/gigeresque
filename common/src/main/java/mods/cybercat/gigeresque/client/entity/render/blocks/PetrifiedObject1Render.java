package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRendererConfig;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.block.animators.Petrified1Animator;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect1Entity;
import net.minecraft.resources.ResourceLocation;

public class PetrifiedObject1Render extends AzBlockEntityRenderer<PetrifiedOjbect1Entity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/aquatic_chestburster/aquatic_chestburster.geo.json");

    public PetrifiedObject1Render() {
        super(
                AzBlockEntityRendererConfig.<PetrifiedOjbect1Entity>builder(MODEL, EntityTextures.AQUATIC_CHESTBURSTER_PETRIFIED)
                        .setAnimatorProvider(Petrified1Animator::new)
                        .build()
        );
    }
}
