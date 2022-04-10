package mods.cybercat.gigeresque.client.entity.model.blocks;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GigBlockItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SarcophagusItemModel extends AnimatedGeoModel<GigBlockItem> {

	@Override
	public Identifier getAnimationFileLocation(GigBlockItem animatable) {
		return new Identifier(Gigeresque.MOD_ID, "animations/sarcophagus.animation.json");
	}

	@Override
	public Identifier getModelLocation(GigBlockItem object) {
		return new Identifier(Gigeresque.MOD_ID, "geo/sarcophagus.geo.json");
	}

	@Override
	public Identifier getTextureLocation(GigBlockItem object) {
		return new Identifier(Gigeresque.MOD_ID, "textures/block/sarcophagus.png");
	}

}
