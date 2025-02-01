package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRendererConfig;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.animators.StatueGooAnimator;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageGooEntity;
import net.minecraft.resources.ResourceLocation;

public class SarcophagusGooRender extends AzBlockEntityRenderer<AlienStorageGooEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/block/sarcophagus/sarcophagus.geo.json");

    private static final ResourceLocation TEXTURE = Constants.modResource("textures/block/sarcophagus/sarcophagus.png");

    public SarcophagusGooRender() {
        super(
                AzBlockEntityRendererConfig.<AlienStorageGooEntity>builder(MODEL, TEXTURE)
                        .setAnimatorProvider(StatueGooAnimator::new)
                        .build()
        );
    }

}
