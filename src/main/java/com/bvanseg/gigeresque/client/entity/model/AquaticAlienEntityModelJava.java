package com.bvanseg.gigeresque.client.entity.model;

import com.bvanseg.gigeresque.client.entity.animation.EntityAnimationsJava;
import com.bvanseg.gigeresque.client.entity.texture.EntityTexturesJava;
import com.bvanseg.gigeresque.common.entity.impl.AquaticAlienEntityJava;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class AquaticAlienEntityModelJava extends AnimatedGeoModel<AquaticAlienEntityJava> {
    @Override
    public Identifier getModelLocation(AquaticAlienEntityJava object) {
        return EntityModelsJava.AQUATIC_ALIEN;
    }

    @Override
    public Identifier getTextureLocation(AquaticAlienEntityJava object) {
        return EntityTexturesJava.AQUATIC_ALIEN;
    }

    @Override
    public Identifier getAnimationFileLocation(AquaticAlienEntityJava animatable) {
        return EntityAnimationsJava.AQUATIC_ALIEN;
    }
}
