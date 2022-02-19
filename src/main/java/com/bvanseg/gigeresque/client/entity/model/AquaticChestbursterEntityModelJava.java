package com.bvanseg.gigeresque.client.entity.model;

import com.bvanseg.gigeresque.client.entity.animation.EntityAnimationsJava;
import com.bvanseg.gigeresque.client.entity.texture.EntityTexturesJava;
import com.bvanseg.gigeresque.common.entity.impl.AquaticChestbursterEntityJava;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.List;

@Environment(EnvType.CLIENT)
public class AquaticChestbursterEntityModelJava extends AnimatedGeoModel<AquaticChestbursterEntityJava> {
    @Override
    public Identifier getModelLocation(AquaticChestbursterEntityJava object) {
        return EntityModelsJava.AQUATIC_CHESTBURSTER;
    }

    @Override
    public Identifier getTextureLocation(AquaticChestbursterEntityJava object) {
        return EntityTexturesJava.AQUATIC_CHESTBURSTER;
    }

    @Override
    public Identifier getAnimationFileLocation(AquaticChestbursterEntityJava animatable) {
        return EntityAnimationsJava.AQUATIC_CHESTBURSTER;
    }

    @Override
    public void setLivingAnimations(AquaticChestbursterEntityJava entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        var neck = getAnimationProcessor().getBone("neck");
        List<EntityModelData> extraDataList = customPredicate.getExtraDataOfType(EntityModelData.class);
        if (extraDataList.isEmpty()) return;
        var extraData = extraDataList.get(0);
        neck.setRotationY(extraData.netHeadYaw * (((float)Math.PI) / 340f));
    }
}
