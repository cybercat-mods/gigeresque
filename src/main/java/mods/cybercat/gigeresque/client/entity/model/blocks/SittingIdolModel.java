package mods.cybercat.gigeresque.client.entity.model.blocks;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.block.entity.IdolStorageEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SittingIdolModel extends GeoModel<IdolStorageEntity> {

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

	@Override
	public RenderType getRenderType(IdolStorageEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}

}
