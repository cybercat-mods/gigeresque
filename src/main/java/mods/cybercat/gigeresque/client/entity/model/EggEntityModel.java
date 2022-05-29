package mods.cybercat.gigeresque.client.entity.model;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.impl.AlienEggEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

@Environment(EnvType.CLIENT)
public class EggEntityModel extends AnimatedTickingGeoModel<AlienEggEntity> {
	@Override
	public Identifier getModelResource(AlienEggEntity object) {
		return EntityModels.EGG;
	}

	@Override
	public Identifier getTextureResource(AlienEggEntity object) {
		return object.isHatching() || object.isHatched() ? new Identifier(Gigeresque.MOD_ID, "textures/entity/egg/egg_active.png"): EntityTextures.EGG;
	}

	@Override
	public Identifier getAnimationResource(AlienEggEntity animatable) {
		return EntityAnimations.EGG;
	}
}
