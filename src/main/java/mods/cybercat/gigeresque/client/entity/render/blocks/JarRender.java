package mods.cybercat.gigeresque.client.entity.render.blocks;

import mods.cybercat.gigeresque.client.entity.model.blocks.JarModel;
import mods.cybercat.gigeresque.common.block.entity.JarStorageEntity;
import mod.azure.azurelib.renderer.GeoBlockRenderer;

public class JarRender extends GeoBlockRenderer<JarStorageEntity> {
	public JarRender() {
		super(new JarModel());
	}

}