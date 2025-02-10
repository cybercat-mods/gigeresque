package mods.cybercat.gigeresque.common.entity.helper.managers.animations.aqua;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticChestbursterEntity;

public class AquaticChestbursterAnimManager {

    public static void handleAnimations(AquaticChestbursterEntity chestbursterEntity) {
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

    public static void handleAggroMovementAnimations(AquaticChestbursterEntity chestbursterEntity) {
        if (chestbursterEntity.isInWater()) {
            GigCommonMethods.setAnimation(chestbursterEntity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(chestbursterEntity.animationDispatcher::sendRushSlither);
        }
    }

    public static void handleMovementAnimations(AquaticChestbursterEntity chestbursterEntity) {
        if (chestbursterEntity.isAggressive()) {
            handleAggroMovementAnimations(chestbursterEntity);
        } else if (chestbursterEntity.isInWater()) {
            GigCommonMethods.setAnimation(chestbursterEntity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(chestbursterEntity.animationDispatcher::sendSlither);
        }
    }

    public static void handleIdleAnimations(AquaticChestbursterEntity chestbursterEntity) {
        if (chestbursterEntity.isInWater()) {
            GigCommonMethods.setAnimation(chestbursterEntity.animationDispatcher::sendIdleWater);
        } else {
            GigCommonMethods.setAnimation(chestbursterEntity.animationDispatcher::sendIdleLand);
        }
    }
}
