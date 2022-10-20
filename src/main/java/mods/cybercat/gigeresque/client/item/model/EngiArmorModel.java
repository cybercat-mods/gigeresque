package mods.cybercat.gigeresque.client.item.model;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.item.EngiArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EngiArmorModel extends AnimatedGeoModel<EngiArmorItem> {
	
	ResourceLocation animation = new ResourceLocation(Gigeresque.MOD_ID, "animations/engiarmor.animation.json");
	ResourceLocation model = new ResourceLocation(Gigeresque.MOD_ID, "geo/engiarmor.json");
	ResourceLocation texture = new ResourceLocation(Gigeresque.MOD_ID, "textures/armor/engiarmor.png");
	
	@Override
	public ResourceLocation getAnimationResource(EngiArmorItem animatable) {
		return animation;
	}

	@Override
	public ResourceLocation getModelResource(EngiArmorItem object) {
		return model;
	}

	@Override
	public ResourceLocation getTextureResource(EngiArmorItem object) {
		return texture;
	}


}
