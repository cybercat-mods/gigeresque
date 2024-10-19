package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.model.DefaultedEntityGeoModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerAlienEntity;

@Environment(EnvType.CLIENT)
public class RunnerAlienEntityModel extends DefaultedEntityGeoModel<RunnerAlienEntity> {

    public RunnerAlienEntityModel() {
        super(Constants.modResource("runner_alien/runner_alien"), true);
    }

    @Override
    public RenderType getRenderType(RunnerAlienEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
