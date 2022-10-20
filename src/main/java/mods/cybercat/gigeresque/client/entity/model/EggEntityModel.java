package mods.cybercat.gigeresque.client.entity.model;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.AlienEggEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

@Environment(EnvType.CLIENT)
public class EggEntityModel extends AnimatedTickingGeoModel<AlienEggEntity> {
	@Override
	public ResourceLocation getModelResource(AlienEggEntity object) {
		return EntityModels.EGG;
	}

	@Override
	public ResourceLocation getTextureResource(AlienEggEntity object) {
		return object.isHatching() || object.isHatched() ? EntityTextures.EGG_ACTIVE : EntityTextures.EGG;
	}

	@Override
	public ResourceLocation getAnimationResource(AlienEggEntity animatable) {
		return EntityAnimations.EGG;
	}
}
