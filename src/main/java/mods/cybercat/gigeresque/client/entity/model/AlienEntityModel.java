package mods.cybercat.gigeresque.client.entity.model;

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
        var progress = Math.max(0, Math.min(1- (object.getGrowth() / object.getMaxGrowth()), 1));
        return object.isPassedOut() ? EntityTextures.ALIEN_STATIS : progress > 0 ? EntityTextures.ALIEN_YOUNG :EntityTextures.ALIEN;
    }

    @Override
    public RenderType getRenderType(ClassicAlienEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

}
