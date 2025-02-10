package mods.cybercat.gigeresque.common.entity.helper.managers.animations.runner;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerbursterEntity;

public class RunnerbursterAnimManager {

    public static void handleAnimations(RunnerbursterEntity runnerbursterEntity) {
        if (runnerbursterEntity.isDeadOrDying()) {
            GigCommonMethods.setAnimation(runnerbursterEntity.animationDispatcher::sendDeath);
            return;
        }
        if (runnerbursterEntity.moveAnalysis.isMoving()) {
            handleMovementAnimations(runnerbursterEntity);
        } else {
            handleIdleAnimations(runnerbursterEntity);
        }
    }

    public static void handleAggroMovementAnimations(RunnerbursterEntity runnerbursterEntity) {
        if (runnerbursterEntity.isInWater()) {
            GigCommonMethods.setAnimation(runnerbursterEntity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(runnerbursterEntity.animationDispatcher::sendRun);
        }
    }

    public static void handleMovementAnimations(RunnerbursterEntity runnerbursterEntity) {
        if (runnerbursterEntity.isAggressive()) {
            handleAggroMovementAnimations(runnerbursterEntity);
        } else if (runnerbursterEntity.isInWater()) {
            GigCommonMethods.setAnimation(runnerbursterEntity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(runnerbursterEntity.animationDispatcher::sendRun);
        }
    }

    public static void handleIdleAnimations(RunnerbursterEntity runnerbursterEntity) {
        GigCommonMethods.setAnimation(runnerbursterEntity.animationDispatcher::sendIdle);
    }
}
