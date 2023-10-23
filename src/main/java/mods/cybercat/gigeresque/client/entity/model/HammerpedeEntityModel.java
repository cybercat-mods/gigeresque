package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.mutant.HammerpedeEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class HammerpedeEntityModel extends DefaultedEntityGeoModel<HammerpedeEntity> {

    public HammerpedeEntityModel() {
        super(Constants.modResource("hammerpede/hammerpede"), false);
    }

    @Override
    public RenderType getRenderType(HammerpedeEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
