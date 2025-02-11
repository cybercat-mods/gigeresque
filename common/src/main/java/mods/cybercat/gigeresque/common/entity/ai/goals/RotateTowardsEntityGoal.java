package mods.cybercat.gigeresque.common.entity.ai.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class RotateTowardsEntityGoal extends LookAtPlayerGoal {

    public RotateTowardsEntityGoal(Mob mob, Class<? extends LivingEntity> lookAtType, float lookDistance) {
        super(mob, lookAtType, lookDistance);
    }

    public RotateTowardsEntityGoal(Mob mob, Class<? extends LivingEntity> lookAtType, float lookDistance, float probability) {
        super(mob, lookAtType, lookDistance, probability, false);
    }

    @Override
    public boolean canUse() {
        if (this.mob instanceof AlienEntity alienEntity && alienEntity.stasisManager.isStasis()) {
            return false;
        }
        return super.canUse();
    }
}
