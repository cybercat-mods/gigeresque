package mods.cybercat.gigeresque.client.entity.model;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.impl.ChestbursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import mod.azure.azurelib.model.DefaultedEntityGeoModel;

@Environment(EnvType.CLIENT)
public class ChestbursterEntityModel extends DefaultedEntityGeoModel<ChestbursterEntity> {

	public ChestbursterEntityModel() {
		super(new ResourceLocation(Gigeresque.MOD_ID, "chestburster/chestburster"), true);
	}

	@Override
	public RenderType getRenderType(ChestbursterEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}
}
