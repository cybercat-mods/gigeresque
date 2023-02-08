package mods.cybercat.gigeresque.client.entity.model;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.impl.RunnerbursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import mod.azure.azurelib.model.DefaultedEntityGeoModel;

@Environment(EnvType.CLIENT)
public class RunnerbursterEntityModel extends DefaultedEntityGeoModel<RunnerbursterEntity> {

	public RunnerbursterEntityModel() {
		super(new ResourceLocation(Gigeresque.MOD_ID, "runnerburster/runnerburster"), true);
	}

	@Override
	public RenderType getRenderType(RunnerbursterEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}
}
