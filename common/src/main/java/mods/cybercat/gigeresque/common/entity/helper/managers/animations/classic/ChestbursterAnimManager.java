package mods.cybercat.gigeresque.common.entity.helper.managers.animations.classic;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;

public class ChestbursterAnimManager {

    public static void handleAnimations(ChestbursterEntity chestbursterEntity) {
        if (chestbursterEntity.isDeadOrDying()) {
            GigCommonMethods.setAnimation(chestbursterEntity.animationDispatcher::sendDeath);
            return;
        }
        if (chestbursterEntity.moveAnalysis.isMoving()) {
            handleMovementAnimations(chestbursterEntity);
        } else {
            handleIdleAnimations(chestbursterEntity);
        }
    }

    public static void handleAggroMovementAnimations(ChestbursterEntity chestbursterEntity) {
        if (chestbursterEntity.isInWater()) {
            GigCommonMethods.setAnimation(chestbursterEntity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(chestbursterEntity.animationDispatcher::sendRushSlither);
        }
    }

    public static void handleMovementAnimations(ChestbursterEntity chestbursterEntity) {
        if (chestbursterEntity.isAggressive()) {
            handleAggroMovementAnimations(chestbursterEntity);
        } else if (chestbursterEntity.isInWater()) {
            GigCommonMethods.setAnimation(chestbursterEntity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(chestbursterEntity.animationDispatcher::sendSlither);
        }
    }

    public static void handleIdleAnimations(ChestbursterEntity chestbursterEntity) {
        GigCommonMethods.setAnimation(chestbursterEntity.animationDispatcher::sendIdle);
    }
}
