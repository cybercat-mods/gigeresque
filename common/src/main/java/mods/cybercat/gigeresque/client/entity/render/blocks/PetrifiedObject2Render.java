package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRendererConfig;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.block.animators.Petrified2Animator;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect2Entity;
import net.minecraft.resources.ResourceLocation;

public class PetrifiedObject2Render extends AzBlockEntityRenderer<PetrifiedOjbect2Entity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/chestburster/chestburster.geo.json");

    public PetrifiedObject2Render() {
        super(
                AzBlockEntityRendererConfig.<PetrifiedOjbect2Entity>builder(MODEL, EntityTextures.CHESTBURSTER_PETRIFIED)
                        .setAnimatorProvider(Petrified2Animator::new)
                        .build()
        );
    }
}
