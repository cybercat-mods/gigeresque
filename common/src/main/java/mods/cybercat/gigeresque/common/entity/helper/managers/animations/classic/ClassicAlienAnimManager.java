package mods.cybercat.gigeresque.common.entity.helper.managers.animations.classic;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;

public class ClassicAlienAnimManager {

    public static void handleAnimations(ClassicAlienEntity classicAlienEntity) {
        if (classicAlienEntity.isDeadOrDying()) {
            GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendDeath);
            return;
        }
        if (classicAlienEntity.isHissing() && !classicAlienEntity.stasisManager.isStasis()) {
            GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendHiss);
        }
        if (classicAlienEntity.isExecuting()) {
            GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendExecutionCarry);
            return;
        }
        if (classicAlienEntity.moveAnalysis.isMoving()) {
            handleMovementAnimations(classicAlienEntity);
        } else {
            handleIdleAnimations(classicAlienEntity);
        }
    }

    private static boolean visuallyCrawling(ClassicAlienEntity alien) {
        return alien.crawlingManager.isCrawling() || alien.climbingManager.climbing;
    }

    public static void handleMovementAnimations(ClassicAlienEntity classicAlienEntity) {
        if (classicAlienEntity.isVehicle()) {
            if (classicAlienEntity.getFirstPassenger().isAlive()) {
                GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendKidnap);
            } else {
                GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendUnkidnap);
            }
        }
        if (classicAlienEntity.isAggressive()) {
            handleAggroMovementAnimations(classicAlienEntity);
        } else if (visuallyCrawling(classicAlienEntity)) {
            GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendCrawl);
        } else if (classicAlienEntity.isUnderWater()) {
            GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendSwim);
        } else if (classicAlienEntity.isFleeing()) {
            GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendRun);
        } else {
            GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendWalk);
        }
    }

    public static void handleAggroMovementAnimations(ClassicAlienEntity classicAlienEntity) {
        if (classicAlienEntity.isVehicle()) {
            if (classicAlienEntity.getFirstPassenger().isAlive()) {
                GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendKidnap);
            } else {
                GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendUnkidnap);
            }
        }
        if (visuallyCrawling(classicAlienEntity)) {
            GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendCrawl);
        } else if (classicAlienEntity.isUnderWater()) {
            GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendSwim);
        } else if (classicAlienEntity.isFleeing()) {
            GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendRun);
        } else {
            GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendRun);
        }
    }

    public static void handleIdleAnimations(ClassicAlienEntity classicAlienEntity) {
        if (classicAlienEntity.isVehicle()) {
            if (classicAlienEntity.getFirstPassenger().isAlive()) {
                GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendKidnap);
            } else {
                GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendUnkidnap);
            }
        }
        if (classicAlienEntity.stasisManager.isStasis()) {
            GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendStatisEnter);
        } else if (classicAlienEntity.searchingManager.isSearching()) {
            GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendAmbient);
        } else if (visuallyCrawling(classicAlienEntity)) {
            GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendCrawl);
        } else if (classicAlienEntity.isUnderWater()) {
            GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(classicAlienEntity.animationDispatcher::sendIdleLand);
        }
    }
}
