package mods.cybercat.gigeresque.client.entity.model;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.impl.HammerpedeEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import mod.azure.azurelib.model.DefaultedEntityGeoModel;

@Environment(EnvType.CLIENT)
public class HammerpedeEntityModel extends DefaultedEntityGeoModel<HammerpedeEntity> {

	public HammerpedeEntityModel() {
		super(new ResourceLocation(Gigeresque.MOD_ID, "hammerpede/hammerpede"), false);
	}

	@Override
	public RenderType getRenderType(HammerpedeEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}
}
