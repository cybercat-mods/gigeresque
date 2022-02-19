package com.bvanseg.gigeresque.client.entity.model;

import com.bvanseg.gigeresque.client.entity.animation.EntityAnimationsJava;
import com.bvanseg.gigeresque.client.entity.texture.EntityTexturesJava;
import com.bvanseg.gigeresque.common.entity.impl.FacehuggerEntityJava;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class FacehuggerEntityModelJava extends AnimatedGeoModel<FacehuggerEntityJava> {
    @Override
    public Identifier getModelLocation(FacehuggerEntityJava object) {
        return EntityModelsJava.FACEHUGGER;
    }

    @Override
    public Identifier getTextureLocation(FacehuggerEntityJava object) {
        return EntityTexturesJava.FACEHUGGER;
    }

    @Override
    public Identifier getAnimationFileLocation(FacehuggerEntityJava animatable) {
        return EntityAnimationsJava.FACEHUGGER;
    }
}
