package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.common.api.client.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticChestbursterEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class AquaticChestbursterEntityModel extends DefaultedEntityGeoModel<AquaticChestbursterEntity> {

    public AquaticChestbursterEntityModel() {
        super(Constants.modResource("aquatic_chestburster/aquatic_chestburster"), false);
    }

    @Override
    public RenderType getRenderType(AquaticChestbursterEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
