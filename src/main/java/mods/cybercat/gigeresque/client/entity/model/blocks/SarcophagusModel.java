package mods.cybercat.gigeresque.client.entity.model.blocks;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SarcophagusModel extends AnimatedGeoModel<AlienStorageEntity> {

	@Override
	public ResourceLocation getAnimationResource(AlienStorageEntity animatable) {
		return new ResourceLocation(Gigeresque.MOD_ID, "animations/sarcophagus.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(AlienStorageEntity object) {
		return new ResourceLocation(Gigeresque.MOD_ID, "geo/sarcophagus.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(AlienStorageEntity object) {
		return new ResourceLocation(Gigeresque.MOD_ID, "textures/block/sarcophagus.png");
	}

}
