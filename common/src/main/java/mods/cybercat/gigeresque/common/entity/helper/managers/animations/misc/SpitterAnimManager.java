package mods.cybercat.gigeresque.common.entity.helper.managers.animations.misc;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.misc.SpitterEntity;

public class SpitterAnimManager {

    public static void handleAnimations(SpitterEntity spitterEntity) {
        if (spitterEntity.isDeadOrDying()) {
            GigCommonMethods.setAnimation(spitterEntity.animationDispatcher::sendDeath);
            return;
        }
        if (spitterEntity.moveAnalysis.isMoving()) {
            handleMovementAnimations(spitterEntity);
        } else {
            handleIdleAnimations(spitterEntity);
        }
    }

    public static void handleAggroMovementAnimations(SpitterEntity spitterEntity) {
        if (spitterEntity.crawlingManager.isCrawling()) {
            GigCommonMethods.setAnimation(spitterEntity.animationDispatcher::sendCrawl);
        } else if (spitterEntity.isUnderWater()) {
            GigCommonMethods.setAnimation(spitterEntity.animationDispatcher::sendRushSwim);
        } else {
            GigCommonMethods.setAnimation(spitterEntity.animationDispatcher::sendRun);
        }
    }

    public static void handleMovementAnimations(SpitterEntity spitterEntity) {
        if (spitterEntity.isAggressive()) {
            handleAggroMovementAnimations(spitterEntity);
        } else if (spitterEntity.crawlingManager.isCrawling()) {
            GigCommonMethods.setAnimation(spitterEntity.animationDispatcher::sendCrawl);
        } else if (spitterEntity.isUnderWater()) {
            GigCommonMethods.setAnimation(spitterEntity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(spitterEntity.animationDispatcher::sendWalk);
        }
    }

    public static void handleIdleAnimations(SpitterEntity spitterEntity) {
        if (spitterEntity.crawlingManager.isCrawling()) {
            GigCommonMethods.setAnimation(spitterEntity.animationDispatcher::sendCrawl);
        } else if (spitterEntity.isUnderWater()) {
            GigCommonMethods.setAnimation(spitterEntity.animationDispatcher::sendIdleWater);
        } else {
            GigCommonMethods.setAnimation(spitterEntity.animationDispatcher::sendIdle);
        }
    }
}
