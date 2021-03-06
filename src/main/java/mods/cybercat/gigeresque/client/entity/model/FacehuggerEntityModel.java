package mods.cybercat.gigeresque.client.entity.model;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.FacehuggerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

@Environment(EnvType.CLIENT)
public class FacehuggerEntityModel extends AnimatedTickingGeoModel<FacehuggerEntity> {
	@Override
	public Identifier getModelResource(FacehuggerEntity object) {
		return EntityModels.FACEHUGGER;
	}

	@Override
	public Identifier getTextureResource(FacehuggerEntity object) {
		return EntityTextures.FACEHUGGER;
	}

	@Override
	public Identifier getAnimationResource(FacehuggerEntity animatable) {
		return EntityAnimations.FACEHUGGER;
	}
}
