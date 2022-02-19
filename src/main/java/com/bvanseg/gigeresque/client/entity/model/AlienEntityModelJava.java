package com.bvanseg.gigeresque.client.entity.model;

import com.bvanseg.gigeresque.client.entity.animation.EntityAnimationsJava;
import com.bvanseg.gigeresque.client.entity.texture.EntityTexturesJava;
import com.bvanseg.gigeresque.common.entity.impl.ClassicAlienEntityJava;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.List;

@Environment(EnvType.CLIENT)
public class AlienEntityModelJava extends AnimatedGeoModel<ClassicAlienEntityJava> {

    @Override
    public Identifier getModelLocation(ClassicAlienEntityJava object) {
        return EntityModelsJava.ALIEN;
    }

    @Override
    public Identifier getTextureLocation(ClassicAlienEntityJava object) {
        return EntityTexturesJava.ALIEN;
    }

    @Override
    public Identifier getAnimationFileLocation(ClassicAlienEntityJava animatable) {
        return EntityAnimationsJava.ALIEN;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void setLivingAnimations(ClassicAlienEntityJava entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        var neck = getAnimationProcessor().getBone("neck");
        List<EntityModelData> extraDataList = customPredicate.getExtraDataOfType(EntityModelData.class);
        if (extraDataList.isEmpty()) return;
        var extraData = extraDataList.get(0);
        neck.setRotationY(extraData.netHeadYaw * (((float) Math.PI) / 340f));
    }

}
