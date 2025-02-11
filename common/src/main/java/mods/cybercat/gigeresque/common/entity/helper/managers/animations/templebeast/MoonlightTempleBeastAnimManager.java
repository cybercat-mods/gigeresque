package mods.cybercat.gigeresque.common.entity.helper.managers.animations.templebeast;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.MoonlightHorrorTempleBeastEntity;

public class MoonlightTempleBeastAnimManager {

    public static void handleAnimations(MoonlightHorrorTempleBeastEntity entity) {
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

    public static void handleAggroMovementAnimations(MoonlightHorrorTempleBeastEntity entity) {
        if (entity.isUnderWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendRushSwim);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendRun);
        }
    }

    public static void handleMovementAnimations(MoonlightHorrorTempleBeastEntity entity) {
        if (entity.isAggressive()) {
            handleAggroMovementAnimations(entity);
        } else if (entity.isUnderWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendWalk);
        }
    }

    public static void handleIdleAnimations(MoonlightHorrorTempleBeastEntity entity) {
        if (entity.isUnderWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendIdle);
        }
    }
}
