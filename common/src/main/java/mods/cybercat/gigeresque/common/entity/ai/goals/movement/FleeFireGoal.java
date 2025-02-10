package mods.cybercat.gigeresque.common.entity.ai.goals.movement;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;

public class FleeFireGoal extends Goal {

    protected final AlienEntity mob;

    public FleeFireGoal(AlienEntity mob) {
        this.mob = mob;
    }

    @Override
    public void start() {
        this.mob.setAggressive(false);
        this.mob.setFleeingStatus(true);
    }

    @Override
    public void stop() {}

    @Override
    public boolean canUse() {
        if (this.mob.stasisManager.isStasis()) {
            return false;
        }

        if (this.mob.level().dimensionType().piglinSafe()) {
            return false;
        }

        return this.mob.isAlive();
    }

    @Override
    public void tick() {
        var mobPos = this.mob.blockPosition();
        var searchRadius = 5;
        var isLavaNearby = false;
        var runAwayDirection = new Vec3(0, 0, 0);

        for (
            var pos : BlockPos.betweenClosed(mobPos.offset(-searchRadius, -1, -searchRadius), mobPos.offset(searchRadius, 1, searchRadius))
        ) {
            if (this.mob.level().getBlockState(pos).is(GigTags.ALIEN_REPELLENTS)) {
                isLavaNearby = true;
                var lavaPos = Vec3.atCenterOf(pos);
                runAwayDirection = runAwayDirection.add(this.mob.position().subtract(lavaPos).normalize());
            }
        }

        if (isLavaNearby && this.mob.getNavigation().isDone()) {
            var panicPos = this.mob.position().add(runAwayDirection.normalize().scale(20.0));
            var mobPosition = this.mob.position();
            var targetPos = mobPosition.add(panicPos);
            this.mob.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, 1.15);
        }
    }
}
