package mods.cybercat.gigeresque.client.entity.model.blocks;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.entity.JarStorageEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class JarModel extends AnimatedGeoModel<JarStorageEntity> {

	@Override
	public ResourceLocation getAnimationResource(JarStorageEntity animatable) {
		return new ResourceLocation(Gigeresque.MOD_ID, "animations/jar.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(JarStorageEntity object) {
		return new ResourceLocation(Gigeresque.MOD_ID, "geo/jar.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(JarStorageEntity object) {
		return new ResourceLocation(Gigeresque.MOD_ID, "textures/block/jar.png");
	}

}
