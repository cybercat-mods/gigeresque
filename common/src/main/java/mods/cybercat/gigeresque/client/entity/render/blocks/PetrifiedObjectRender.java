package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRendererConfig;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.block.animators.PetrifiedAnimator;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbectEntity;

public class PetrifiedObjectRender extends AzBlockEntityRenderer<PetrifiedOjbectEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/block/egg_petrified/egg_petrified.geo.json");

    public PetrifiedObjectRender() {
        super(
            AzBlockEntityRendererConfig.<PetrifiedOjbectEntity>builder(MODEL, EntityTextures.EGG_PETRIFIED)
                .setAnimatorProvider(PetrifiedAnimator::new)
                .build()
        );
    }
}
