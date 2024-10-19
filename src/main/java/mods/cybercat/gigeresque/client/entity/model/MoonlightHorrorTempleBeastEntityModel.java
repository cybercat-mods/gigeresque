package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.model.DefaultedEntityGeoModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.MoonlightHorrorTempleBeastEntity;

@Environment(EnvType.CLIENT)
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
