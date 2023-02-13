package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.AquaticAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class AquaticAlienEntityModel extends DefaultedEntityGeoModel<AquaticAlienEntity> {

	public AquaticAlienEntityModel() {
		super(Constants.modResource("aquatic_alien/aquatic_alien"), false);
	}

	@Override
	public RenderType getRenderType(AquaticAlienEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}
}
