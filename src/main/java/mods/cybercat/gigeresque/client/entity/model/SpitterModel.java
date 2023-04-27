package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.extra.SpitterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class SpitterModel extends DefaultedEntityGeoModel<SpitterEntity> {

	public SpitterModel() {
		super(Constants.modResource("spitter/spitter"), false);
	}

	@Override
	public RenderType getRenderType(SpitterEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}

}
