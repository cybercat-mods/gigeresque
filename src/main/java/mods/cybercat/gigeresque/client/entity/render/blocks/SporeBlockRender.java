package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.renderer.GeoBlockRenderer;

import mods.cybercat.gigeresque.client.entity.model.blocks.SporeBlockModel;
import mods.cybercat.gigeresque.common.block.entity.SporeBlockEntity;

public class SporeBlockRender extends GeoBlockRenderer<SporeBlockEntity> {

    public SporeBlockRender() {
        super(new SporeBlockModel());
    }

}
