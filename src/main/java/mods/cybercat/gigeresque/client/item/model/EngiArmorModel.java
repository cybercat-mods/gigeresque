package mods.cybercat.gigeresque.client.item.model;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.item.EngiArmorItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EngiArmorModel extends AnimatedGeoModel<EngiArmorItem> {
	
	Identifier animation = new Identifier(Gigeresque.MOD_ID, "animations/engiarmor.animation.json");
	Identifier model = new Identifier(Gigeresque.MOD_ID, "geo/engiarmor.json");
	Identifier texture = new Identifier(Gigeresque.MOD_ID, "textures/armor/engiarmor.png");
	
	@Override
	public Identifier getAnimationResource(EngiArmorItem animatable) {
		return animation;
	}

	@Override
	public Identifier getModelResource(EngiArmorItem object) {
		return model;
	}

	@Override
	public Identifier getTextureResource(EngiArmorItem object) {
		return texture;
	}


}
