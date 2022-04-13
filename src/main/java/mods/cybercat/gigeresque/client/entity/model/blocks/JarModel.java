package mods.cybercat.gigeresque.client.entity.model.blocks;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.entity.JarStorageEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class JarModel extends AnimatedGeoModel<JarStorageEntity> {

	@Override
	public Identifier getAnimationFileLocation(JarStorageEntity animatable) {
		return new Identifier(Gigeresque.MOD_ID, "animations/jar.animation.json");
	}

	@Override
	public Identifier getModelLocation(JarStorageEntity object) {
		return new Identifier(Gigeresque.MOD_ID, "geo/jar.geo.json");
	}

	@Override
	public Identifier getTextureLocation(JarStorageEntity object) {
		return new Identifier(Gigeresque.MOD_ID, "textures/block/jar.png");
	}

}
