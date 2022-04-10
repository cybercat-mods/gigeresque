package mods.cybercat.gigeresque.client.entity.model.blocks;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SarcophagusModel extends AnimatedGeoModel<AlienStorageEntity> {

	@Override
	public Identifier getAnimationFileLocation(AlienStorageEntity animatable) {
		return new Identifier(Gigeresque.MOD_ID, "animations/sarcophagus.animation.json");
	}

	@Override
	public Identifier getModelLocation(AlienStorageEntity object) {
		return new Identifier(Gigeresque.MOD_ID, "geo/sarcophagus.geo.json");
	}

	@Override
	public Identifier getTextureLocation(AlienStorageEntity object) {
		return new Identifier(Gigeresque.MOD_ID, "textures/block/sarcophagus.png");
	}

}
