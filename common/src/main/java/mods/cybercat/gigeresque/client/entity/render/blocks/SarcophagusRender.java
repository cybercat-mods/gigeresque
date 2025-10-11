package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.common.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.common.render.block.AzBlockEntityRendererConfig;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.animators.StatueAnimator;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageEntity;

public class SarcophagusRender extends AzBlockEntityRenderer<AlienStorageEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/block/sarcophagus/sarcophagus.geo.json");

    private static final ResourceLocation TEXTURE = Constants.modResource("textures/block/sarcophagus/sarcophagus.png");

    public SarcophagusRender() {
        super(
            AzBlockEntityRendererConfig.<AlienStorageEntity>builder(MODEL, TEXTURE)
                .setAnimatorProvider(StatueAnimator::new)
                .build()
        );
    }

}
