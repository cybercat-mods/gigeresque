package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.model.DefaultedEntityGeoModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;

@Environment(EnvType.CLIENT)
public class FacehuggerEntityModel extends DefaultedEntityGeoModel<FacehuggerEntity> {

    public FacehuggerEntityModel() {
        super(Constants.modResource("facehugger/facehugger"), false);
    }

    @Override
    public RenderType getRenderType(FacehuggerEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
