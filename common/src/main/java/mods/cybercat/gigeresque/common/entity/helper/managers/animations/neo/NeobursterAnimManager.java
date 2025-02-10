package mods.cybercat.gigeresque.common.entity.helper.managers.animations.neo;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeobursterEntity;

public class NeobursterAnimManager {

    public static void handleAnimations(NeobursterEntity neobursterEntity) {
        if (neobursterEntity.isDeadOrDying()) {
            GigCommonMethods.setAnimation(neobursterEntity.animationDispatcher::sendDeath);
            return;
        }
        if (neobursterEntity.moveAnalysis.isMoving()) {
            handleMovementAnimations(neobursterEntity);
        } else {
            handleIdleAnimations(neobursterEntity);
        }
    }

    public static void handleAggroMovementAnimations(NeobursterEntity neobursterEntity) {
        if (neobursterEntity.isInWater()) {
            GigCommonMethods.setAnimation(neobursterEntity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(neobursterEntity.animationDispatcher::sendRun);
        }
    }

    public static void handleMovementAnimations(NeobursterEntity neobursterEntity) {
        if (neobursterEntity.isAggressive()) {
            handleAggroMovementAnimations(neobursterEntity);
        } else if (neobursterEntity.isInWater()) {
            GigCommonMethods.setAnimation(neobursterEntity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(neobursterEntity.animationDispatcher::sendRun);
        }
    }

    public static void handleIdleAnimations(NeobursterEntity neobursterEntity) {
        GigCommonMethods.setAnimation(neobursterEntity.animationDispatcher::sendIdle);
    }
}
