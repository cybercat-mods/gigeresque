package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.common.api.client.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class PopperEntityModel extends DefaultedEntityGeoModel<PopperEntity> {

    public PopperEntityModel() {
        super(Constants.modResource("popper/popper"), false);
    }

    @Override
    public RenderType getRenderType(PopperEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
