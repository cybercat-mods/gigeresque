package mods.cybercat.gigeresque.common.entity.helper.managers.animations.rom;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.rom.RomAlienEntity;

public class RomAlienAnimManager {

    public static void handleAnimations(RomAlienEntity entity) {
        if (entity.isDeadOrDying()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendDeath);
            return;
        }
        if (entity.isHissing() && !entity.stasisManager.isStasis()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendHiss);
        }
        if (entity.isVehicle()) {
            handleVehicleAnimations(entity);
        }
        if (entity.moveAnalysis.isMoving()) {
            handleMovementAnimations(entity);
        } else {
            handleIdleAnimations(entity);
        }
    }

    public static void handleVehicleAnimations(RomAlienEntity entity) {
        if (entity.isExecuting()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendExecutionCarry);
        } else if (entity.moveAnalysis.isMoving()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendWalkCarrying);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendKidnap);
        }
    }

    public static void handleMovementAnimations(RomAlienEntity entity) {
        if (entity.isAggressive()) {
            handleAggroMovementAnimations(entity);
        } else if (entity.isInWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendWalk);
        }
    }

    public static void handleAggroMovementAnimations(RomAlienEntity entity) {
        if (entity.isInWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendRun);
        }
    }

    public static void handleIdleAnimations(RomAlienEntity entity) {
        if (entity.stasisManager.isStasis()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendStatisEnter);
        } else if (entity.searchingManager.isSearching()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendAmbient);
        } else if (entity.isInWater()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendIdleLand);
        }
    }
}
