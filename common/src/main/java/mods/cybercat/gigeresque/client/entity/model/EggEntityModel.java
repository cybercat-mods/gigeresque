package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.common.api.client.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class EggEntityModel extends DefaultedEntityGeoModel<AlienEggEntity> {

    public EggEntityModel() {
        super(Constants.modResource("egg/egg"), false);
    }

    @Override
    public ResourceLocation getTextureResource(AlienEggEntity object) {
        return object.isHatching() || object.isHatched() ? EntityTextures.EGG_ACTIVE : EntityTextures.EGG;
    }

    @Override
    public RenderType getRenderType(AlienEggEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }
}
