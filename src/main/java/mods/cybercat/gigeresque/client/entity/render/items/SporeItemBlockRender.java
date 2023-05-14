package mods.cybercat.gigeresque.client.entity.render.items;

import mod.azure.azurelib.renderer.GeoItemRenderer;
import mods.cybercat.gigeresque.client.entity.model.items.SporeItemBlockModel;
import mods.cybercat.gigeresque.common.item.GigBlockItem;

public class SporeItemBlockRender extends GeoItemRenderer<GigBlockItem> {

	public SporeItemBlockRender() {
		super(new SporeItemBlockModel());
	}

}