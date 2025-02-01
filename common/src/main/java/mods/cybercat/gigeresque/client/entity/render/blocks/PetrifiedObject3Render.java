package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRendererConfig;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.block.animators.Petrified3Animator;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect3Entity;
import net.minecraft.resources.ResourceLocation;

public class PetrifiedObject3Render extends AzBlockEntityRenderer<PetrifiedOjbect3Entity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/neoburster/neoburster.geo.json");

    public PetrifiedObject3Render() {
        super(
                AzBlockEntityRendererConfig.<PetrifiedOjbect3Entity>builder(MODEL, EntityTextures.NEOBURSTER_PETRIFIED)
                        .setAnimatorProvider(Petrified3Animator::new)
                        .build()
        );
    }
}
