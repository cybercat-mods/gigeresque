package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.common.api.client.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.MoonlightHorrorTempleBeastEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class MoonlightHorrorTempleBeastEntityModel extends DefaultedEntityGeoModel<MoonlightHorrorTempleBeastEntity> {

    public MoonlightHorrorTempleBeastEntityModel() {
        super(Constants.modResource("moonlighthorrortemplebeast/moonlighthorrortemplebeast"), false);
    }

    @Override
    public ResourceLocation getTextureResource(MoonlightHorrorTempleBeastEntity object) {
        return object.isPassedOut() ? EntityTextures.MOONLIGHTHORRORTEMPLEBEAST_STATIS : EntityTextures.MOONLIGHTHORRORTEMPLEBEAST;
    }

    @Override
    public RenderType getRenderType(MoonlightHorrorTempleBeastEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

}
