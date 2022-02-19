package com.bvanseg.gigeresque.client.entity.model;

import com.bvanseg.gigeresque.client.entity.animation.EntityAnimationsJava;
import com.bvanseg.gigeresque.client.entity.texture.EntityTexturesJava;
import com.bvanseg.gigeresque.common.entity.impl.RunnerAlienEntityJava;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.List;

@Environment(EnvType.CLIENT)
public class RunnerAlienEntityModelJava extends AnimatedGeoModel<RunnerAlienEntityJava> {
    @Override
    public Identifier getModelLocation(RunnerAlienEntityJava object) {
        return EntityModelsJava.RUNNER_ALIEN;
    }

    @Override
    public Identifier getTextureLocation(RunnerAlienEntityJava object) {
        return EntityTexturesJava.RUNNER_ALIEN;
    }

    @Override
    public Identifier getAnimationFileLocation(RunnerAlienEntityJava animatable) {
        return EntityAnimationsJava.RUNNER_ALIEN;
    }

    @Override
    public void setLivingAnimations(RunnerAlienEntityJava entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        var neck = getAnimationProcessor().getBone("neck");
        List<EntityModelData> extraDataList = customPredicate.getExtraDataOfType(EntityModelData.class);
        if (extraDataList.isEmpty()) return;
        var extraData = extraDataList.get(0);
        neck.setRotationY(extraData.netHeadYaw * ((float) Math.PI) / 340f);
    }
}
