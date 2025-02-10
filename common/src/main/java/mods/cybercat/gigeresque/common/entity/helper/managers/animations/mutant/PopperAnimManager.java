package mods.cybercat.gigeresque.common.entity.helper.managers.animations.mutant;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;

public class PopperAnimManager {

    public static void handleAnimations(PopperEntity entity) {
        if (entity.isDeadOrDying()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendDeath);
            return;
        }
        if (entity.moveAnalysis.isMoving()) {
            handleMovementAnimations(entity);
        } else {
            handleIdleAnimations(entity);
        }
    }

    public static void handleAggroMovementAnimations(PopperEntity entity) {
        if (entity.isInWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendSwim);
        } else {
            if (entity.getEntityData().get(entity.STATE) == 0) {
                GigCommonMethods.setAnimation(entity.animationDispatcher::sendRun);
            } else {
                GigCommonMethods.setAnimation(entity.animationDispatcher::sendCharge);
            }
        }
    }

    public static void handleMovementAnimations(PopperEntity entity) {
        if (entity.isAggressive()) {
            handleAggroMovementAnimations(entity);
        } else if (entity.isInWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendWalk);
        }
    }

    public static void handleIdleAnimations(PopperEntity entity) {
        if (entity.isInWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendIdleWater);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendIdle);
        }
    }
}
