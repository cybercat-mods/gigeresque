package mods.cybercat.gigeresque.common.entity.helper.managers.animations.neo;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphAdolescentEntity;

public class NeomorphAdolescentAnimManager {

    public static void handleAnimations(NeomorphAdolescentEntity adolescentEntity) {
        if (adolescentEntity.isDeadOrDying()) {
            GigCommonMethods.setAnimation(adolescentEntity.animationDispatcher::sendDeath);
            return;
        }
        if (adolescentEntity.moveAnalysis.isMoving()) {
            handleMovementAnimations(adolescentEntity);
        } else {
            handleIdleAnimations(adolescentEntity);
        }
    }

    public static void handleAggroMovementAnimations(NeomorphAdolescentEntity adolescentEntity) {
        if (adolescentEntity.isUnderWater()) {
            GigCommonMethods.setAnimation(adolescentEntity.animationDispatcher::sendRushSwim);
        } else {
            GigCommonMethods.setAnimation(adolescentEntity.animationDispatcher::sendRun);
        }
    }

    public static void handleMovementAnimations(NeomorphAdolescentEntity adolescentEntity) {
        if (adolescentEntity.isAggressive()) {
            handleAggroMovementAnimations(adolescentEntity);
        } else if (adolescentEntity.isUnderWater()) {
            GigCommonMethods.setAnimation(adolescentEntity.animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(adolescentEntity.animationDispatcher::sendWalk);
        }
    }

    public static void handleIdleAnimations(NeomorphAdolescentEntity adolescentEntity) {
        if (adolescentEntity.isUnderWater()) {
            GigCommonMethods.setAnimation(adolescentEntity.animationDispatcher::sendIdleWater);
        } else {
            GigCommonMethods.setAnimation(adolescentEntity.animationDispatcher::sendIdle);
        }
    }
}
