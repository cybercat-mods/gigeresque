package mods.cybercat.gigeresque.client.entity.model.blocks;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SarcophagusModel extends AnimatedGeoModel<AlienStorageEntity> {

	@Override
	public Identifier getAnimationResource(AlienStorageEntity animatable) {
		return new Identifier(Gigeresque.MOD_ID, "animations/sarcophagus.animation.json");
	}

	@Override
	public Identifier getModelResource(AlienStorageEntity object) {
		return new Identifier(Gigeresque.MOD_ID, "geo/sarcophagus.geo.json");
	}

	@Override
	public Identifier getTextureResource(AlienStorageEntity object) {
		return new Identifier(Gigeresque.MOD_ID, "textures/block/sarcophagus.png");
	}

}
