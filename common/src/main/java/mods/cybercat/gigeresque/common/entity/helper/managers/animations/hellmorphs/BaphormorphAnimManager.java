package mods.cybercat.gigeresque.common.entity.helper.managers.animations.hellmorphs;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.BaphomorphEntity;

public class BaphormorphAnimManager {

    public static void handleAnimations(BaphomorphEntity baphomorphEntity) {
        if (baphomorphEntity.isDeadOrDying()) {
            GigCommonMethods.setAnimation(baphomorphEntity.animationDispatcher::sendDeath);
            return;
        }
        if (baphomorphEntity.moveAnalysis.isMoving()) {
            handleMovementAnimations(baphomorphEntity);
        } else {
            handleIdleAnimations(baphomorphEntity);
        }
    }

    public static void handleAggroMovementAnimations(BaphomorphEntity baphomorphEntity) {
        if (baphomorphEntity.isUnderWater()) {
            GigCommonMethods.setAnimation(baphomorphEntity.animationDispatcher::sendRushSwim);
        } else {
            GigCommonMethods.setAnimation(baphomorphEntity.animationDispatcher::sendRun);
        }
    }

    public static void handleMovementAnimations(BaphomorphEntity baphomorphEntity) {
        if (baphomorphEntity.isAggressive()) {
            handleAggroMovementAnimations(baphomorphEntity);
        } else if (baphomorphEntity.isUnderWater()) {
            GigCommonMethods.setAnimation(baphomorphEntity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(baphomorphEntity.animationDispatcher::sendWalk);
        }
    }

    public static void handleIdleAnimations(BaphomorphEntity baphomorphEntity) {
        if (baphomorphEntity.isUnderWater()) {
            GigCommonMethods.setAnimation(baphomorphEntity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(baphomorphEntity.animationDispatcher::sendIdle);
        }
    }
}
