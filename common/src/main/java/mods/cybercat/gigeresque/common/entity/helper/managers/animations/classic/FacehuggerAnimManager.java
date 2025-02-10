package mods.cybercat.gigeresque.common.entity.helper.managers.animations.classic;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;

public class FacehuggerAnimManager {

    public static void handleAnimations(FacehuggerEntity entity) {
        if (entity.isDeadOrDying() || entity.isInfertile()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendDeath);
            return;
        }
        if (!entity.isVehicle()) {
            if (entity.moveAnalysis.isMoving()) {
                handleMovementAnimations(entity);
            } else {
                handleIdleAnimations(entity);
            }
        }
    }

    public static void handleMovementAnimations(FacehuggerEntity entity) {
        if (entity.isAggressive()) {
            handleAggroMovementAnimations(entity);
        } else if (entity.isInWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendCrawl);
        }
    }

    public static void handleAggroMovementAnimations(FacehuggerEntity entity) {
        if (entity.isInWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendRushSwim);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendCrawlRush);
        }
    }

    public static void handleIdleAnimations(FacehuggerEntity entity) {
        if (entity.isInWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendIdleWater);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendIdleLand);
        }
    }

}
