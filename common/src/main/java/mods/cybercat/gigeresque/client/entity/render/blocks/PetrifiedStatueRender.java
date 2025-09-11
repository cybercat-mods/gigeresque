package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRendererConfig;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.block.animators.PetrifiedStatueAnimator;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedStatueEntity;
import net.minecraft.resources.ResourceLocation;

public class PetrifiedStatueRender extends AzBlockEntityRenderer<PetrifiedStatueEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/alien/alien.geo.json");

    public PetrifiedStatueRender() {
        super(
                AzBlockEntityRendererConfig.<PetrifiedStatueEntity>builder(MODEL, EntityTextures.ALIEN_STASIS)
                        .setAnimatorProvider(PetrifiedStatueAnimator::new)
                        .build()
        );
    }
}
