package mods.cybercat.gigeresque.client.entity.model.blocks;

import mod.azure.azurelib.model.GeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.block.entity.PetrifiedOjbectEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class PetrifiedObjectModel extends GeoModel<PetrifiedOjbectEntity> {

    @Override
    public ResourceLocation getModelResource(PetrifiedOjbectEntity petrifiedOjbectEntity) {
        return Constants.modResource("geo/entity/egg/egg.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PetrifiedOjbectEntity petrifiedOjbectEntity) {
        return EntityTextures.EGG_PETRIFIED;
    }

    @Override
    public ResourceLocation getAnimationResource(PetrifiedOjbectEntity petrifiedOjbectEntity) {
        return Constants.modResource("animations/entity/egg/egg.animation.json");
    }

    @Override
    public RenderType getRenderType(PetrifiedOjbectEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
