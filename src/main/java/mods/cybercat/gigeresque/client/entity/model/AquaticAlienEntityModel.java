package mods.cybercat.gigeresque.client.entity.model;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.AquaticAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

@Environment(EnvType.CLIENT)
public class AquaticAlienEntityModel extends AnimatedTickingGeoModel<AquaticAlienEntity> {
	@Override
	public Identifier getModelLocation(AquaticAlienEntity object) {
		return EntityModels.AQUATIC_ALIEN;
	}

	@Override
	public Identifier getTextureLocation(AquaticAlienEntity object) {
		return EntityTextures.AQUATIC_ALIEN;
	}

	@Override
	public Identifier getAnimationFileLocation(AquaticAlienEntity animatable) {
		return EntityAnimations.AQUATIC_ALIEN;
	}
}
