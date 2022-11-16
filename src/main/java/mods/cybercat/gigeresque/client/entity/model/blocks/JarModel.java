package mods.cybercat.gigeresque.client.entity.model.blocks;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.block.entity.JarStorageEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class JarModel extends AnimatedGeoModel<JarStorageEntity> {

	@Override
	public ResourceLocation getAnimationResource(JarStorageEntity animatable) {
		return EntityAnimations.JAR;
	}

	@Override
	public ResourceLocation getModelResource(JarStorageEntity object) {
		return EntityModels.JAR;
	}

	@Override
	public ResourceLocation getTextureResource(JarStorageEntity object) {
		return EntityTextures.JAR;
	}

}
