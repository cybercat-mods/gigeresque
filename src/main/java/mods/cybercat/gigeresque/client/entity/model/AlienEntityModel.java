package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class AlienEntityModel extends DefaultedEntityGeoModel<ClassicAlienEntity> {

    public AlienEntityModel() {
        super(Constants.modResource("alien/alien"), false);
    }

    @Override
    public ResourceLocation getTextureResource(ClassicAlienEntity object) {
        return object.isPassedOut() == true ? EntityTextures.ALIEN_STATIS : EntityTextures.ALIEN;
    }

    @Override
    public RenderType getRenderType(ClassicAlienEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

    @Override
    public void setCustomAnimations(ClassicAlienEntity animatable, long instanceId, AnimationState<ClassicAlienEntity> animationState) {

        super.setCustomAnimations(animatable, instanceId, animationState);
    }

}
