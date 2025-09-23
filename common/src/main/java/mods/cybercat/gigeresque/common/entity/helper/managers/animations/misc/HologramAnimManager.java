package mods.cybercat.gigeresque.common.entity.helper.managers.animations.misc;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.misc.HologramEntity;

public class HologramAnimManager {
    public static void handleAnimations(HologramEntity entity) {
        if (entity.getDistanceState() == 2) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendStage2);
        } else if (entity.getDistanceState() == 3) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendStage3);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendStage1);
        }
    }
}
