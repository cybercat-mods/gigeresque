package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.common.api.client.renderer.GeoBlockRenderer;
import mods.cybercat.gigeresque.client.entity.model.blocks.SarcophagusSporeModel;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageSporeEntity;

public class SarcophagusSporeRender extends GeoBlockRenderer<AlienStorageSporeEntity> {
    public SarcophagusSporeRender() {
        super(new SarcophagusSporeModel());
    }

}