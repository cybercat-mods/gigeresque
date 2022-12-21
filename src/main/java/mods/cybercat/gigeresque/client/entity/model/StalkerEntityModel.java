package mods.cybercat.gigeresque.client.entity.model;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.impl.StalkerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

@Environment(EnvType.CLIENT)
public class StalkerEntityModel extends DefaultedEntityGeoModel<StalkerEntity> {

	public StalkerEntityModel() {
		super(new ResourceLocation(Gigeresque.MOD_ID, "stalker/stalker"), false);
	}

	@Override
	public RenderType getRenderType(StalkerEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}
}
