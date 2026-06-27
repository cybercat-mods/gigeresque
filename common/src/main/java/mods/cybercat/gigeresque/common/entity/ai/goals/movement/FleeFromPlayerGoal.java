package mods.cybercat.gigeresque.common.entity.ai.goals.movement;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class FleeFromPlayerGoal extends Goal {

    private final AlienEntity mob;

    private final double fleeDistance;

    private final double speed;

    private Player nearestPlayer = null;

    public FleeFromPlayerGoal(AlienEntity mob, double fleeDistance, double speed) {
        this.mob = mob;
        this.fleeDistance = fleeDistance;
        this.speed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        nearestPlayer = findNearestPlayer();
        return nearestPlayer != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (nearestPlayer == null || !nearestPlayer.isAlive())
            return false;
        return this.mob.distanceToSqr(nearestPlayer) < fleeDistance * fleeDistance;
    }

    @Override
    public void start() {
        flee();
    }

    @Override
    public void tick() {
        nearestPlayer = findNearestPlayer();
        if (nearestPlayer != null) {
            flee();
        }
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        nearestPlayer = null;
    }

    private void flee() {
        if (nearestPlayer == null)
            return;
        Vec3 mobPos = this.mob.position();
        Vec3 playerPos = nearestPlayer.position();
        Vec3 fleeDir = mobPos.subtract(playerPos).normalize();

        Vec3 fleeTarget = mobPos.add(fleeDir.scale(16.0));

        this.mob.getNavigation()
            .moveTo(
                fleeTarget.x,
                fleeTarget.y,
                fleeTarget.z,
                speed
            );
    }

    private Player findNearestPlayer() {
        List<Player> players = this.mob.level()
            .getEntitiesOfClass(
                Player.class,
                this.mob.getBoundingBox().inflate(fleeDistance),
                player -> !player.isCreative() && !player.isSpectator() && player.isAlive()
            );

        Player nearest = null;
        double nearestDist = Double.MAX_VALUE;

        for (Player player : players) {
            double dist = this.mob.distanceToSqr(player);
            if (dist < nearestDist) {
                nearestDist = dist;
                nearest = player;
            }
        }
        return nearest;
    }
}
