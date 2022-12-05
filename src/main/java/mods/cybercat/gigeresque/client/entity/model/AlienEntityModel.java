package mods.cybercat.gigeresque.client.entity.model;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.ClassicAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class AlienEntityModel extends GeoModel<ClassicAlienEntity> {

	@Override
	public ResourceLocation getModelResource(ClassicAlienEntity object) {
		return EntityModels.ALIEN;
	}

	@Override
	public ResourceLocation getTextureResource(ClassicAlienEntity object) {
		return object.isStatis() == true ? EntityTextures.ALIEN_STATIS : EntityTextures.ALIEN;
	}

	@Override
	public ResourceLocation getAnimationResource(ClassicAlienEntity animatable) {
		return EntityAnimations.ALIEN;
	}

}
