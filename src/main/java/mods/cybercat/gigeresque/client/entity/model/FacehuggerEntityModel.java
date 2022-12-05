package mods.cybercat.gigeresque.client.entity.model;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.FacehuggerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class FacehuggerEntityModel extends GeoModel<FacehuggerEntity> {
	@Override
	public ResourceLocation getModelResource(FacehuggerEntity object) {
		return EntityModels.FACEHUGGER;
	}

	@Override
	public ResourceLocation getTextureResource(FacehuggerEntity object) {
		return EntityTextures.FACEHUGGER;
	}

	@Override
	public ResourceLocation getAnimationResource(FacehuggerEntity animatable) {
		return EntityAnimations.FACEHUGGER;
	}
}
