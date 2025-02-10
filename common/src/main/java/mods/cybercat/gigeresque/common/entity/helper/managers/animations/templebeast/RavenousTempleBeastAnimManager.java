package mods.cybercat.gigeresque.common.entity.helper.managers.animations.templebeast;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.RavenousTempleBeastEntity;

public class RavenousTempleBeastAnimManager {

    public static void handleAnimations(RavenousTempleBeastEntity entity) {
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

    public static void handleAggroMovementAnimations(RavenousTempleBeastEntity entity) {
        if (entity.isInWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendRushSwim);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendRun);
        }
    }

    public static void handleMovementAnimations(RavenousTempleBeastEntity entity) {
        if (entity.isAggressive()) {
            handleAggroMovementAnimations(entity);
        } else if (entity.isInWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendWalk);
        }
    }

    public static void handleIdleAnimations(RavenousTempleBeastEntity entity) {
        if (entity.isInWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendIdle);
        }
    }
}
