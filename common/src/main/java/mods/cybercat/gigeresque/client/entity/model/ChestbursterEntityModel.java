package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.common.api.client.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ChestbursterEntityModel extends DefaultedEntityGeoModel<ChestbursterEntity> {

    public ChestbursterEntityModel() {
        super(Constants.modResource("chestburster/chestburster"), false);
    }

    @Override
    public RenderType getRenderType(ChestbursterEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
