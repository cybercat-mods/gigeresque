package com.bvanseg.gigeresque.client.entity.model;

import com.bvanseg.gigeresque.client.entity.animation.EntityAnimations;
import com.bvanseg.gigeresque.client.entity.texture.EntityTextures;
import com.bvanseg.gigeresque.common.entity.impl.AquaticAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class AquaticAlienEntityModel extends AnimatedGeoModel<AquaticAlienEntity> {
    @Override
    public Identifier getModelLocation(AquaticAlienEntity object) {
        return EntityModels.AQUATIC_ALIEN;
    }

    @Override
    public Identifier getTextureLocation(AquaticAlienEntity object) {
        return EntityTextures.AQUATIC_ALIEN;
    }

    @Override
    public Identifier getAnimationFileLocation(AquaticAlienEntity animatable) {
        return EntityAnimations.AQUATIC_ALIEN;
    }
}
