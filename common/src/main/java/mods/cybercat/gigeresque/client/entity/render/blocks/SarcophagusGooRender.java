package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.common.api.client.renderer.GeoBlockRenderer;
import mods.cybercat.gigeresque.client.entity.model.blocks.SarcophagusGooModel;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageGooEntity;

public class SarcophagusGooRender extends GeoBlockRenderer<AlienStorageGooEntity> {
    public SarcophagusGooRender() {
        super(new SarcophagusGooModel());
    }

}