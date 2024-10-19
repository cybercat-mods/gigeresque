package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.model.DefaultedEntityGeoModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;

@Environment(EnvType.CLIENT)
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
