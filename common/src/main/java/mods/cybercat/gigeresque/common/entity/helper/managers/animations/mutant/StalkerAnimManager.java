package mods.cybercat.gigeresque.common.entity.helper.managers.animations.mutant;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;

public class StalkerAnimManager {

    public static void handleAnimations(StalkerEntity entity) {
        if (entity.isDeadOrDying()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendDeath);
            return;
        }
        if (entity.getLastDamageSource() != null && entity.hurtDuration > 0 && !entity.isInWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendHurt);
        }
        if (entity.moveAnalysis.isMoving()) {
            handleMovementAnimations(entity);
        } else {
            handleIdleAnimations(entity);
        }
    }

    public static void handleAggroMovementAnimations(StalkerEntity entity) {
        if (entity.isInWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendRun);
        }
    }

    public static void handleMovementAnimations(StalkerEntity entity) {
        if (entity.isAggressive() && !entity.swinging) {
            handleAggroMovementAnimations(entity);
        } else if (entity.isInWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendWalk);
        }
    }

    public static void handleIdleAnimations(StalkerEntity entity) {
        if (!entity.swinging) {
            if (entity.isInWater()) {
                GigCommonMethods.setAnimation(entity.animationDispatcher::sendIdleWater);
            } else {
                GigCommonMethods.setAnimation(entity.animationDispatcher::sendIdle);
            }
        }
    }
}
