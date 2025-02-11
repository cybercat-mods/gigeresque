package mods.cybercat.gigeresque.common.entity.helper.managers.animations.hellmorphs;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.HellmorphRunnerEntity;

public class HellmorphAnimManager {

    public static void handleAnimations(HellmorphRunnerEntity hellmorphRunnerEntity) {
        if (hellmorphRunnerEntity.isDeadOrDying()) {
            GigCommonMethods.setAnimation(hellmorphRunnerEntity.animationDispatcher::sendDeath);
            return;
        }
        if (hellmorphRunnerEntity.moveAnalysis.isMoving()) {
            handleMovementAnimations(hellmorphRunnerEntity);
        } else {
            handleIdleAnimations(hellmorphRunnerEntity);
        }
    }

    public static void handleAggroMovementAnimations(HellmorphRunnerEntity hellmorphRunnerEntity) {
        if (hellmorphRunnerEntity.isUnderWater()) {
            GigCommonMethods.setAnimation(hellmorphRunnerEntity.animationDispatcher::sendRushSwim);
        } else {
            GigCommonMethods.setAnimation(hellmorphRunnerEntity.animationDispatcher::sendRun);
        }
    }

    public static void handleMovementAnimations(HellmorphRunnerEntity hellmorphRunnerEntity) {
        if (hellmorphRunnerEntity.isAggressive()) {
            handleAggroMovementAnimations(hellmorphRunnerEntity);
        } else if (hellmorphRunnerEntity.isUnderWater()) {
            GigCommonMethods.setAnimation(hellmorphRunnerEntity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(hellmorphRunnerEntity.animationDispatcher::sendWalk);
        }
    }

    public static void handleIdleAnimations(HellmorphRunnerEntity hellmorphRunnerEntity) {
        if (hellmorphRunnerEntity.isUnderWater()) {
            GigCommonMethods.setAnimation(hellmorphRunnerEntity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(hellmorphRunnerEntity.animationDispatcher::sendIdle);
        }
    }
}
