package mods.cybercat.gigeresque.client.entity.model;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.impl.PopperEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import mod.azure.azurelib.model.DefaultedEntityGeoModel;

@Environment(EnvType.CLIENT)
public class PopperEntityModel extends DefaultedEntityGeoModel<PopperEntity> {

	public PopperEntityModel() {
		super(new ResourceLocation(Gigeresque.MOD_ID, "popper/popper"), false);
	}

	@Override
	public RenderType getRenderType(PopperEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}
}
