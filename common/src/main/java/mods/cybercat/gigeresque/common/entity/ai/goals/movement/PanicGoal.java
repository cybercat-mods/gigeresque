package mods.cybercat.gigeresque.common.entity.ai.goals.movement;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class PanicGoal extends Goal {

    protected final AlienEntity mob;

    public PanicGoal(AlienEntity mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return this.mob.getLastHurtByMob() != null && this.mob.isAlive();
    }

    @Override
    public void start() {
        this.mob.setAggressive(false);
        this.mob.setFleeingStatus(true);
    }

    @Override
    public void stop() {
        this.mob.setFleeingStatus(false);
    }

    @Override
    public void tick() {
        if (this.mob.getNavigation().isDone() && this.mob.getLastHurtByMob() != null) {
            var lastAttacker = this.mob.getLastHurtByMob();
            var attackerPos = lastAttacker.position();
            var randomDirection = this.getRandomDirectionAwayFrom(attackerPos).normalize().scale(30.0);
            var mobPosition = this.mob.position();
            var targetPos = mobPosition.add(randomDirection);

            this.mob.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, 1.0);
        }
    }

    private Vec3 getRandomDirectionAwayFrom(Vec3 attackerPos) {
        var mobPosition = this.mob.position();
        var directionAwayFromAttacker = mobPosition.subtract(attackerPos);
        var randomOffsetX = this.mob.getRandom().nextDouble() - 0.5;
        var randomOffsetZ = this.mob.getRandom().nextDouble() - 0.5;

        return directionAwayFromAttacker.add(randomOffsetX, 0, randomOffsetZ);
    }
}
