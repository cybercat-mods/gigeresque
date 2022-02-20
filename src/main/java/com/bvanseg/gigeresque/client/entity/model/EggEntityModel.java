package com.bvanseg.gigeresque.client.entity.model;

import com.bvanseg.gigeresque.client.entity.animation.EntityAnimations;
import com.bvanseg.gigeresque.client.entity.texture.EntityTextures;
import com.bvanseg.gigeresque.common.entity.impl.AlienEggEntity;

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
