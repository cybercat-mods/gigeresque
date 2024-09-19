package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.common.api.client.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.BaphomorphEntity;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.HellmorphRunnerEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class HellmorphRunnerEntityModel extends DefaultedEntityGeoModel<HellmorphRunnerEntity> {

    public HellmorphRunnerEntityModel() {
        super(Constants.modResource("hellmorph_runner/hellmorph_runner"), false);
    }

    @Override
    public ResourceLocation getTextureResource(HellmorphRunnerEntity object) {
        return EntityTextures.HELLMORPH_RUNNER;
    }

    @Override
    public RenderType getRenderType(HellmorphRunnerEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

}
