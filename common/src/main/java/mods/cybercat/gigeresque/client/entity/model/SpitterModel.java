package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.common.api.client.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.misc.SpitterEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class SpitterModel extends DefaultedEntityGeoModel<SpitterEntity> {

    public SpitterModel() {
        super(Constants.modResource("spitter/spitter"), false);
    }

    @Override
    public RenderType getRenderType(SpitterEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

}
