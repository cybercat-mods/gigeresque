package mods.cybercat.gigeresque.client.entity.render.blocks;

import mod.azure.azurelib.renderer.GeoBlockRenderer;
import mods.cybercat.gigeresque.client.entity.model.blocks.SarcophagusModel;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageEntity;

public class SarcophagusRender extends GeoBlockRenderer<AlienStorageEntity> {
	public SarcophagusRender() {
		super(new SarcophagusModel());
	}

}