package mods.cybercat.gigeresque.client.entity.model.blocks;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.entity.IdolStorageEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SittingIdolModel extends AnimatedGeoModel<IdolStorageEntity> {

	@Override
	public ResourceLocation getAnimationResource(IdolStorageEntity animatable) {
		return new ResourceLocation(Gigeresque.MOD_ID, "animations/sittingidol.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(IdolStorageEntity object) {
		return new ResourceLocation(Gigeresque.MOD_ID, "geo/sittingidol.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(IdolStorageEntity object) {
		return new ResourceLocation(Gigeresque.MOD_ID, "textures/block/sittingidol.png");
	}

}
