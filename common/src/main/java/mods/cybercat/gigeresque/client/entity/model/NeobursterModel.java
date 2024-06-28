package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.common.api.client.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeobursterEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class NeobursterModel extends DefaultedEntityGeoModel<NeobursterEntity> {

    public NeobursterModel() {
        super(Constants.modResource("neoburster/neoburster"), false);
    }

    @Override
    public RenderType getRenderType(NeobursterEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

}
