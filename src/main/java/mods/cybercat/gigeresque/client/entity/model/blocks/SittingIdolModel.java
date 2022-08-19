package mods.cybercat.gigeresque.client.entity.model.blocks;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.entity.IdolStorageEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SittingIdolModel extends AnimatedGeoModel<IdolStorageEntity> {

	@Override
	public Identifier getAnimationResource(IdolStorageEntity animatable) {
		return new Identifier(Gigeresque.MOD_ID, "animations/sittingidol.animation.json");
	}

	@Override
	public Identifier getModelResource(IdolStorageEntity object) {
		return new Identifier(Gigeresque.MOD_ID, "geo/sittingidol.geo.json");
	}

	@Override
	public Identifier getTextureResource(IdolStorageEntity object) {
		return new Identifier(Gigeresque.MOD_ID, "textures/block/sittingidol.png");
	}

}
