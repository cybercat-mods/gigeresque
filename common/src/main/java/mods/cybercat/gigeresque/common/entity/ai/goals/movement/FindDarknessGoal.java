package mods.cybercat.gigeresque.common.entity.ai.goals.movement;

import net.minecraft.world.entity.ai.goal.Goal;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

/**
 * TODO: Rewrite this whole thing to seek out caves instead oj just dark spots
 */
public class FindDarknessGoal extends Goal {

    protected final AlienEntity mob;

    public FindDarknessGoal(AlienEntity mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return false;
    }
}
