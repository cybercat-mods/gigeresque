package com.bvanseg.gigeresque.client.entity.model;

import com.bvanseg.gigeresque.client.entity.animation.EntityAnimationsJava;
import com.bvanseg.gigeresque.client.entity.texture.EntityTexturesJava;
import com.bvanseg.gigeresque.common.entity.impl.AlienEggEntityJava;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class EggEntityModelJava extends AnimatedGeoModel<AlienEggEntityJava> {
    @Override
    public Identifier getModelLocation(AlienEggEntityJava object) {
        return EntityModelsJava.EGG;
    }

    @Override
    public Identifier getTextureLocation(AlienEggEntityJava object) {
        return EntityTexturesJava.EGG;
    }

    @Override
    public Identifier getAnimationFileLocation(AlienEggEntityJava animatable) {
        return EntityAnimationsJava.EGG;
    }
}
