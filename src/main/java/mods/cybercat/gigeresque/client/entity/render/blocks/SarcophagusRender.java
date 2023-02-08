package mods.cybercat.gigeresque.client.entity.render.blocks;

import mods.cybercat.gigeresque.client.entity.model.blocks.SarcophagusModel;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageEntity;
import mod.azure.azurelib.renderer.GeoBlockRenderer;

public class SarcophagusRender extends GeoBlockRenderer<AlienStorageEntity> {
	public SarcophagusRender() {
		super(new SarcophagusModel());
	}

}