package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.common.api.client.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.BaphomorphEntity;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.RavenousTempleBeastEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class BaphomorphEntityModel extends DefaultedEntityGeoModel<BaphomorphEntity> {

    public BaphomorphEntityModel() {
        super(Constants.modResource("baphomorph/baphomorph"), false);
    }

    @Override
    public ResourceLocation getTextureResource(BaphomorphEntity object) {
        return EntityTextures.BAPHOMORPH;
    }

    @Override
    public RenderType getRenderType(BaphomorphEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

}
