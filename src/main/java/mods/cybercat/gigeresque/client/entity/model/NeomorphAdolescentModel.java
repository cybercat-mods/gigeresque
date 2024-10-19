package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.model.DefaultedEntityGeoModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphAdolescentEntity;

@Environment(EnvType.CLIENT)
public class NeomorphAdolescentModel extends DefaultedEntityGeoModel<NeomorphAdolescentEntity> {

    public NeomorphAdolescentModel() {
        super(Constants.modResource("neomorph_adolescent/neomorph_adolescent"), false);
    }

    @Override
    public RenderType getRenderType(NeomorphAdolescentEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

}
