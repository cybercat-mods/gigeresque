package mods.cybercat.gigeresque.client.entity.model;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.AlienEggEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class EggEntityModel extends AnimatedGeoModel<AlienEggEntity> {
	@Override
	public Identifier getModelLocation(AlienEggEntity object) {
		return EntityModels.EGG;
	}

	@Override
	public Identifier getTextureLocation(AlienEggEntity object) {
		return EntityTextures.EGG;
	}

	@Override
	public Identifier getAnimationFileLocation(AlienEggEntity animatable) {
		return EntityAnimations.EGG;
	}
}
