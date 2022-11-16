package mods.cybercat.gigeresque.client.entity.model.blocks;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.block.entity.IdolStorageEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SittingIdolModel extends AnimatedGeoModel<IdolStorageEntity> {

	@Override
	public ResourceLocation getAnimationResource(IdolStorageEntity animatable) {
		return EntityAnimations.SITTINGIDOL;
	}

	@Override
	public ResourceLocation getModelResource(IdolStorageEntity object) {
		return EntityModels.SITTINGIDOL;
	}

	@Override
	public ResourceLocation getTextureResource(IdolStorageEntity object) {
		return EntityTextures.SITTINGIDOL;
	}

}
