package mods.cybercat.gigeresque.client.entity.model.blocks;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SarcophagusModel extends GeoModel<AlienStorageEntity> {

	@Override
	public ResourceLocation getAnimationResource(AlienStorageEntity animatable) {
		return EntityAnimations.SARCOPHAGUS;
	}

	@Override
	public ResourceLocation getModelResource(AlienStorageEntity object) {
		return EntityModels.SARCOPHAGUS;
	}

	@Override
	public ResourceLocation getTextureResource(AlienStorageEntity object) {
		return EntityTextures.SARCOPHAGUS;
	}

	@Override
	public RenderType getRenderType(AlienStorageEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}

}
