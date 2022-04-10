package mods.cybercat.gigeresque.client.entity.render.blocks;

import mods.cybercat.gigeresque.client.entity.model.blocks.SarcophagusItemModel;
import mods.cybercat.gigeresque.common.block.GigBlockItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class SarcophagusItemRender extends GeoItemRenderer<GigBlockItem> {
	public SarcophagusItemRender() {
		super(new SarcophagusItemModel());
	}

}
