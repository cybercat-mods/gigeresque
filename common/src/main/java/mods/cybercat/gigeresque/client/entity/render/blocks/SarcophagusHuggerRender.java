package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.common.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.common.render.block.AzBlockEntityRendererConfig;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.animators.StatueHuggerAnimator;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageHuggerEntity;

public class SarcophagusHuggerRender<T extends AlienStorageHuggerEntity> extends AzBlockEntityRenderer<T> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/block/sarcophagus/sarcophagus.geo.json");

    private static final ResourceLocation TEXTURE = Constants.modResource("textures/block/sarcophagus/sarcophagus.png");

    public SarcophagusHuggerRender() {
        super(
            AzBlockEntityRendererConfig.<T>builder(MODEL, TEXTURE)
                .setAnimatorProvider(StatueHuggerAnimator::new)
                .build()
        );
    }

}
