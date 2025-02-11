package mods.cybercat.gigeresque.common.entity.helper.managers.animations.neo;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphEntity;

public class NeomorphAnimManager {

    public static void handleAnimations(NeomorphEntity entity) {
        if (entity.isDeadOrDying()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendDeath);
            return;
        }
        if (entity.isHissing() && !entity.stasisManager.isStasis()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendHiss);
        }
        if (entity.moveAnalysis.isMoving()) {
            handleMovementAnimations(entity);
        } else {
            handleIdleAnimations(entity);
        }
    }

    public static void handleMovementAnimations(NeomorphEntity entity) {
        if (entity.isAggressive()) {
            handleAggroMovementAnimations(entity);
        } else if (entity.crawlingManager.isCrawling()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendCrawl);
        } else if (entity.isUnderWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendWalk);
        }
    }

    public static void handleAggroMovementAnimations(NeomorphEntity entity) {
        if (entity.crawlingManager.isCrawling()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendCrawl);
        } else if (entity.isUnderWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendRun);
        }
    }

    public static void handleIdleAnimations(NeomorphEntity entity) {
        if (entity.stasisManager.isStasis()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendStasisLoop);
        } else if (entity.crawlingManager.isCrawling()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendCrawl);
        } else if (entity.isUnderWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendIdle);
        }
    }
}
