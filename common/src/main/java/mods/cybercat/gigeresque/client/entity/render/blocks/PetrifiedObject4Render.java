package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRendererConfig;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.block.animators.Petrified4Animator;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect4Entity;
import net.minecraft.resources.ResourceLocation;

public class PetrifiedObject4Render extends AzBlockEntityRenderer<PetrifiedOjbect4Entity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/runnerburster/runnerburster.geo.json");

    public PetrifiedObject4Render() {
        super(
                AzBlockEntityRendererConfig.<PetrifiedOjbect4Entity>builder(MODEL, EntityTextures.RUNNERBURSTER_PETRIFIED)
                        .setAnimatorProvider(Petrified4Animator::new)
                        .build()
        );
    }
}
