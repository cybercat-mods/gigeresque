package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.common.api.client.renderer.GeoBlockRenderer;
import mods.cybercat.gigeresque.client.entity.model.blocks.JarModel;
import mods.cybercat.gigeresque.common.block.entity.JarStorageEntity;

public class JarRender extends GeoBlockRenderer<JarStorageEntity> {
    public JarRender() {
        super(new JarModel());
    }

}