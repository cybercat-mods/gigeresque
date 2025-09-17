package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRendererConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class PetrifiedStatueRender<T extends BlockEntity> extends AzBlockEntityRenderer<T> {

    public PetrifiedStatueRender(ResourceLocation model, ResourceLocation texture) {
        super(
                AzBlockEntityRendererConfig.<T>builder(model, texture).build()
        );
    }
}
