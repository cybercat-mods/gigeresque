package mods.cybercat.gigeresque.common.entity.helper.managers.animations.runner;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerAlienEntity;

public class RunnerAnimManager {

    public static void handleAnimations(RunnerAlienEntity runnerAlienEntity) {
        if (runnerAlienEntity.isDeadOrDying()) {
            GigCommonMethods.setAnimation(runnerAlienEntity.animationDispatcher::sendDeath);
            return;
        }
        if (runnerAlienEntity.moveAnalysis.isMoving()) {
            handleMovementAnimations(runnerAlienEntity);
        } else {
            handleIdleAnimations(runnerAlienEntity);
        }
    }

    public static void handleAggroMovementAnimations(RunnerAlienEntity runnerAlienEntity) {
        if (runnerAlienEntity.crawlingManager.isCrawling()) {
            GigCommonMethods.setAnimation(runnerAlienEntity.animationDispatcher::sendCrawl);
        } else if (runnerAlienEntity.isUnderWater()) {
            GigCommonMethods.setAnimation(runnerAlienEntity.animationDispatcher::sendRushSwim);
        } else {
            GigCommonMethods.setAnimation(runnerAlienEntity.animationDispatcher::sendRun);
        }
    }

    public static void handleMovementAnimations(RunnerAlienEntity runnerAlienEntity) {
        if (runnerAlienEntity.isAggressive()) {
            handleAggroMovementAnimations(runnerAlienEntity);
        } else if (runnerAlienEntity.isUnderWater()) {
            GigCommonMethods.setAnimation(runnerAlienEntity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(runnerAlienEntity.animationDispatcher::sendWalk);
        }
    }

    public static void handleIdleAnimations(RunnerAlienEntity runnerAlienEntity) {
        if (runnerAlienEntity.stasisManager.isStasis()) {
            GigCommonMethods.setAnimation(runnerAlienEntity.animationDispatcher::sendStatisEnter);
        } else if (runnerAlienEntity.crawlingManager.isCrawling()) {
            GigCommonMethods.setAnimation(runnerAlienEntity.animationDispatcher::sendCrawl);
        } else if (runnerAlienEntity.isUnderWater()) {
            GigCommonMethods.setAnimation(runnerAlienEntity.animationDispatcher::sendIdleWater);
        } else {
            GigCommonMethods.setAnimation(runnerAlienEntity.animationDispatcher::sendIdleLand);
        }
    }
}
