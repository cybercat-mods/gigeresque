package mods.cybercat.gigeresque.common.entity.helper.managers;

import mods.cybercat.gigeresque.common.entity.ai.nav.GigNavigation;
import mods.cybercat.gigeresque.common.entity.ai.nav.WaterMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.pathfinder.PathType;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class AlienNavigationManager {

    private final GroundPathNavigation groundNavigation;

    private final MoveControl groundMoveControl;

    private final WaterBoundPathNavigation waterNavigation;

    private final WaterMoveControl waterMoveControl;

    public AlienNavigationManager(AlienEntity xenomorph, MoveControl moveControl) {
        this.groundMoveControl = moveControl;
        this.groundNavigation = new GigNavigation(xenomorph, xenomorph.level());

        xenomorph.setPathfindingMalus(PathType.WATER, 0.0F);
        this.waterMoveControl = new WaterMoveControl(xenomorph);
        this.waterNavigation = new WaterBoundPathNavigation(xenomorph, xenomorph.level());
    }

    public void switchToGround(AlienEntity xenomorph) {
        xenomorph.setMoveControl(groundMoveControl);
        xenomorph.setNavigation(groundNavigation);
    }

    public void switchToWater(AlienEntity xenomorph) {
        xenomorph.setMoveControl(waterMoveControl);
        xenomorph.setNavigation(waterNavigation);
    }
}
