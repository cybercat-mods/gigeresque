package mods.cybercat.gigeresque.common.entity.helper.managers.animations.mutant;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.mutant.HammerpedeEntity;

public class HammerpedeAnimManager {

    public static void handleAnimations(HammerpedeEntity entity) {
        if (entity.isDeadOrDying()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendDeath);
            return;
        }
        // if (this.isAggressive()) {
        // GigCommonMethods.setAnimation(entity.animationDispatcher::sendHostile);
        // }
        if (entity.moveAnalysis.isMoving()) {
            handleMovementAnimations(entity);
        } else {
            handleIdleAnimations(entity);
        }
    }

    public static void handleAggroMovementAnimations(HammerpedeEntity entity) {
        if (entity.isUnderWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendWalkHostile);
        }
    }

    public static void handleMovementAnimations(HammerpedeEntity entity) {
        if (entity.isAggressive()) {
            handleAggroMovementAnimations(entity);
        } else if (entity.isUnderWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendWalk);
        }
    }

    public static void handleIdleAnimations(HammerpedeEntity entity) {
        if (!entity.isAggressive()) {
            if (entity.isUnderWater()) {
                GigCommonMethods.setAnimation(entity.animationDispatcher::sendIdleWater);
            } else {
                GigCommonMethods.setAnimation(entity.animationDispatcher::sendIdle);
            }
        }
    }
}
