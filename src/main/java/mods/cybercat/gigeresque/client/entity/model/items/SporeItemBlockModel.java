package mods.cybercat.gigeresque.client.entity.model.items;

import mod.azure.azurelib.model.DefaultedBlockGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.item.GigBlockItem;

public class SporeItemBlockModel extends DefaultedBlockGeoModel<GigBlockItem> {

	public SporeItemBlockModel() {
		super(Constants.modResource("neomorph_spore_pods/neomorph_spore_pods"));
	}

}
