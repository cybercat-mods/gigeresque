package mods.cybercat.gigeresque.client.entity.model;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.AquaticAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

@Environment(EnvType.CLIENT)
public class AquaticAlienEntityModel extends AnimatedTickingGeoModel<AquaticAlienEntity> {
	@Override
	public ResourceLocation getModelResource(AquaticAlienEntity object) {
		return EntityModels.AQUATIC_ALIEN;
	}

	@Override
	public ResourceLocation getTextureResource(AquaticAlienEntity object) {
		return EntityTextures.AQUATIC_ALIEN;
	}

	@Override
	public ResourceLocation getAnimationResource(AquaticAlienEntity animatable) {
		return EntityAnimations.AQUATIC_ALIEN;
	}
}
