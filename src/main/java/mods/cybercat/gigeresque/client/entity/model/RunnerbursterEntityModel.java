package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.model.DefaultedEntityGeoModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerbursterEntity;

@Environment(EnvType.CLIENT)
public class RunnerbursterEntityModel extends DefaultedEntityGeoModel<RunnerbursterEntity> {

    public RunnerbursterEntityModel() {
        super(Constants.modResource("runnerburster/runnerburster"), false);
    }

    @Override
    public RenderType getRenderType(RunnerbursterEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
