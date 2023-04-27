package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticChestbursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class AquaticChestbursterEntityModel extends DefaultedEntityGeoModel<AquaticChestbursterEntity> {

	public AquaticChestbursterEntityModel() {
		super(Constants.modResource("aquatic_chestburster/aquatic_chestburster"), true);
	}

	@Override
	public RenderType getRenderType(AquaticChestbursterEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}
}
