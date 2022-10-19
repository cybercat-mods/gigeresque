package mods.cybercat.gigeresque.client.item.render;

import mods.cybercat.gigeresque.client.item.model.EngiArmorModel;
import mods.cybercat.gigeresque.common.item.EngiArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class EngiArmorRender extends GeoArmorRenderer<EngiArmorItem> {

	public EngiArmorRender() {
		super(new EngiArmorModel());

		this.headBone = "armorHead";
		this.bodyBone = "armorBody";
		this.rightArmBone = "armorRightArm";
		this.leftArmBone = "armorLeftArm";
		this.rightLegBone = "armorLeftLeg";
		this.leftLegBone = "armorRightLeg";
		this.rightBootBone = "armorLeftBoot";
		this.leftBootBone = "armorRightBoot";
	}

}
