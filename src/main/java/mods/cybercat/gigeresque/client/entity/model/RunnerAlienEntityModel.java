package mods.cybercat.gigeresque.client.entity.model;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.impl.RunnerAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import mod.azure.azurelib.model.DefaultedEntityGeoModel;

@Environment(EnvType.CLIENT)
public class RunnerAlienEntityModel extends DefaultedEntityGeoModel<RunnerAlienEntity> {

	public RunnerAlienEntityModel() {
		super(new ResourceLocation(Gigeresque.MOD_ID, "runner_alien/runner_alien"), true);
	}

	@Override
	public RenderType getRenderType(RunnerAlienEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}
}
