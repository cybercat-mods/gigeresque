package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.common.api.client.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class StalkerEntityModel extends DefaultedEntityGeoModel<StalkerEntity> {

    public StalkerEntityModel() {
        super(Constants.modResource("stalker/stalker"), false);
    }

    @Override
    public RenderType getRenderType(StalkerEntity animatable, ResourceLocation texture) {
        return animatable.walkAnimation.speedOld < 0.35F && !animatable.swinging ? RenderType.entityTranslucentCull(getTextureResource(animatable)) : RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
