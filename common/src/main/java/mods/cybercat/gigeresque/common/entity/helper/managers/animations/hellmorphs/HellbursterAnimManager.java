package mods.cybercat.gigeresque.common.entity.helper.managers.animations.hellmorphs;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.HellbursterEntity;

public class HellbursterAnimManager {

    public static void handleAnimations(HellbursterEntity hellbursterEntity) {
        if (hellbursterEntity.isDeadOrDying()) {
            GigCommonMethods.setAnimation(hellbursterEntity.animationDispatcher::sendDeath);
            return;
        }
        if (hellbursterEntity.moveAnalysis.isMoving()) {
            handleMovementAnimations(hellbursterEntity);
        } else {
            handleIdleAnimations(hellbursterEntity);
        }
    }

    public static void handleAggroMovementAnimations(HellbursterEntity hellbursterEntity) {
        if (hellbursterEntity.isInWater()) {
            GigCommonMethods.setAnimation(hellbursterEntity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(hellbursterEntity.animationDispatcher::sendRun);
        }
    }

    public static void handleMovementAnimations(HellbursterEntity hellbursterEntity) {
        if (hellbursterEntity.isAggressive()) {
            handleAggroMovementAnimations(hellbursterEntity);
        } else if (hellbursterEntity.isInWater()) {
            GigCommonMethods.setAnimation(hellbursterEntity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(hellbursterEntity.animationDispatcher::sendRun);
        }
    }

    public static void handleIdleAnimations(HellbursterEntity hellbursterEntity) {
        GigCommonMethods.setAnimation(hellbursterEntity.animationDispatcher::sendIdle);
    }
}
