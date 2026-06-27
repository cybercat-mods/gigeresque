package mods.cybercat.gigeresque.common.entity.ai.goals.movement;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class PatrolForTargetsGoal extends Goal {

    private final AlienEntity mob;

    private final double speed;

    private BlockPos patrolTarget = null;

    private int patrolTimer = 0;

    private int searchTimer = 0;

    public PatrolForTargetsGoal(AlienEntity mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.mob.getTarget() == null
            && !this.mob.stasisManager.isStasis()
            && !this.mob.isVehicle()
            && !this.mob.isAggressive();
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.getTarget() == null
            && !this.mob.stasisManager.isStasis()
            && !this.mob.isVehicle()
            && patrolTarget != null;
    }

    @Override
    public void start() {
        patrolTimer = 0;
        searchTimer = 0;
        patrolTarget = findPatrolPoint();
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        patrolTarget = null;
        patrolTimer = 0;
    }

    @Override
    public void tick() {
        searchTimer++;

        if (searchTimer >= 100 || patrolTarget == null) {
            patrolTarget = findPatrolPoint();
            searchTimer = 0;
        }

        if (patrolTarget == null)
            return;

        Vec3 centerPos = Vec3.atCenterOf(patrolTarget);
        double distanceSq = this.mob.distanceToSqr(centerPos);

        if (distanceSq > 4.0D) {
            patrolTimer++;
            if (patrolTimer >= 40) {
                patrolTimer = 0;
                this.mob.getNavigation()
                    .moveTo(
                        centerPos.x,
                        centerPos.y,
                        centerPos.z,
                        speed
                    );
            }
        } else {
            patrolTarget = findPatrolPoint();
            patrolTimer = 0;
        }
    }

    private BlockPos findPatrolPoint() {
        var random = this.mob.getRandom();
        BlockPos mobPos = this.mob.blockPosition();

        for (int i = 0; i < 10; i++) {
            int x = mobPos.getX() + random.nextInt(48) - 24;
            int y = mobPos.getY() + random.nextInt(6) - 3;
            int z = mobPos.getZ() + random.nextInt(48) - 24;
            BlockPos candidate = new BlockPos(x, y, z);

            if (
                this.mob.level().getBlockState(candidate).isAir()
                    && this.mob.level().getBlockState(candidate.below()).isSolid()
            ) {
                return candidate;
            }
        }
        return null;
    }
}
