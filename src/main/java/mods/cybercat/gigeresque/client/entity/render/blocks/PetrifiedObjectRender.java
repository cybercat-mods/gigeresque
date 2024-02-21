package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.renderer.GeoBlockRenderer;
import mods.cybercat.gigeresque.client.entity.model.blocks.PetrifiedObjectModel;
import mods.cybercat.gigeresque.common.block.entity.PetrifiedOjbectEntity;

public class PetrifiedObjectRender extends GeoBlockRenderer<PetrifiedOjbectEntity> {
    public PetrifiedObjectRender() {
        super(new PetrifiedObjectModel());
    }

}