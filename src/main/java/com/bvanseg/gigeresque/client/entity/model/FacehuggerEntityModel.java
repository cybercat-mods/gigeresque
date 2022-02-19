package com.bvanseg.gigeresque.client.entity.model;

import com.bvanseg.gigeresque.client.entity.animation.EntityAnimations;
import com.bvanseg.gigeresque.client.entity.texture.EntityTextures;
import com.bvanseg.gigeresque.common.entity.impl.FacehuggerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class FacehuggerEntityModel extends AnimatedGeoModel<FacehuggerEntity> {
    @Override
    public Identifier getModelLocation(FacehuggerEntity object) {
        return EntityModels.FACEHUGGER;
    }

    @Override
    public Identifier getTextureLocation(FacehuggerEntity object) {
        return EntityTextures.FACEHUGGER;
    }

    @Override
    public Identifier getAnimationFileLocation(FacehuggerEntity animatable) {
        return EntityAnimations.FACEHUGGER;
    }
}
