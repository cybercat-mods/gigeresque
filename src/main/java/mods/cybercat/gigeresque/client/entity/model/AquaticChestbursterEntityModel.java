package mods.cybercat.gigeresque.client.entity.model;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.impl.AquaticChestbursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import mod.azure.azurelib.model.DefaultedEntityGeoModel;

@Environment(EnvType.CLIENT)
public class AquaticChestbursterEntityModel extends DefaultedEntityGeoModel<AquaticChestbursterEntity> {

	public AquaticChestbursterEntityModel() {
		super(new ResourceLocation(Gigeresque.MOD_ID, "aquatic_chestburster/aquatic_chestburster"), true);
	}

	@Override
	public RenderType getRenderType(AquaticChestbursterEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}
}
