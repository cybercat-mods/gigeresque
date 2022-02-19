package com.bvanseg.gigeresque.client.entity.render;

import com.bvanseg.gigeresque.client.entity.animation.EntityAnimationsJava;
import com.bvanseg.gigeresque.client.entity.model.EntityModelsJava;
import com.bvanseg.gigeresque.client.entity.texture.EntityTexturesJava;
import com.bvanseg.gigeresque.common.entity.impl.ChestbursterEntityJava;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ChestbursterEntityModelJava extends AnimatedGeoModel<ChestbursterEntityJava> {
    @Override
    public Identifier getModelLocation(ChestbursterEntityJava object) {
        return EntityModelsJava.CHESTBURSTER;
    }

    @Override
    public Identifier getTextureLocation(ChestbursterEntityJava object) {
        return EntityTexturesJava.CHESTBURSTER;
    }

    @Override
    public Identifier getAnimationFileLocation(ChestbursterEntityJava animatable) {
        return EntityAnimationsJava.CHESTBURSTER;
    }

    @Override
    public void setLivingAnimations(ChestbursterEntityJava entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        var neck = getAnimationProcessor().getBone("neck");
        List<EntityModelData> extraDataList = customPredicate.getExtraDataOfType(EntityModelData.class);
        if (extraDataList.isEmpty()) return;
        var extraData = extraDataList.get(0);
        neck.setRotationY(extraData.netHeadYaw * (((float) Math.PI) / 340f));
    }
}
