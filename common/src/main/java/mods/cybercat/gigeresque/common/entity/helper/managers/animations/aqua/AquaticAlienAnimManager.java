package mods.cybercat.gigeresque.common.entity.helper.managers.animations.aqua;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticAlienEntity;

public class AquaticAlienAnimManager {

    public static void handleAnimations(AquaticAlienEntity aquaticAlienEntity) {
        if (aquaticAlienEntity.isDeadOrDying()) {
            GigCommonMethods.setAnimation(aquaticAlienEntity.animationDispatcher::sendDeath);
            return;
        }
        if (aquaticAlienEntity.moveAnalysis.isMoving()) {
            handleMovementAnimations(aquaticAlienEntity);
        } else {
            handleIdleAnimations(aquaticAlienEntity);
        }
    }

    public static void handleAggroMovementAnimations(AquaticAlienEntity aquaticAlienEntity) {
        if (aquaticAlienEntity.isInWater()) {
            GigCommonMethods.setAnimation(aquaticAlienEntity.animationDispatcher::sendRushSwim);
        } else {
            GigCommonMethods.setAnimation(aquaticAlienEntity.animationDispatcher::sendCrawlRush);
        }
    }

    public static void handleMovementAnimations(AquaticAlienEntity aquaticAlienEntity) {
        if (aquaticAlienEntity.isAggressive()) {
            handleAggroMovementAnimations(aquaticAlienEntity);
        } else if (aquaticAlienEntity.isInWater()) {
            GigCommonMethods.setAnimation(aquaticAlienEntity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(aquaticAlienEntity.animationDispatcher::sendCrawl);
        }
    }

    public static void handleIdleAnimations(AquaticAlienEntity aquaticAlienEntity) {
        if (aquaticAlienEntity.isInWater()) {
            GigCommonMethods.setAnimation(aquaticAlienEntity.animationDispatcher::sendIdleWater);
        } else {
            GigCommonMethods.setAnimation(aquaticAlienEntity.animationDispatcher::sendIdleLand2);
        }
    }
}
