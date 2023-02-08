package mods.cybercat.gigeresque.client.entity.model;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.impl.FacehuggerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import mod.azure.azurelib.model.DefaultedEntityGeoModel;

@Environment(EnvType.CLIENT)
public class FacehuggerEntityModel extends DefaultedEntityGeoModel<FacehuggerEntity> {

	public FacehuggerEntityModel() {
		super(new ResourceLocation(Gigeresque.MOD_ID, "facehugger/facehugger"), false);
	}

	@Override
	public RenderType getRenderType(FacehuggerEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}
}
