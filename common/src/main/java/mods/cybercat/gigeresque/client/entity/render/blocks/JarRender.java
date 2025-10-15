package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.common.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.common.render.block.AzBlockEntityRendererConfig;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.animators.JarAnimator;
import mods.cybercat.gigeresque.common.block.entity.JarStorageEntity;

public class JarRender extends AzBlockEntityRenderer<JarStorageEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/block/jar/jar.geo.json");

    private static final ResourceLocation TEXTURE = Constants.modResource("textures/block/jar/jar.png");

    public JarRender() {
        super(
            AzBlockEntityRendererConfig.<JarStorageEntity>builder(MODEL, TEXTURE)
                .setAnimatorProvider(JarAnimator::new)
                .build()
        );
    }

}
