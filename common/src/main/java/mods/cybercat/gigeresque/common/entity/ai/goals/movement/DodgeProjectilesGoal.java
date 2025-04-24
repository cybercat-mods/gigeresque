package mods.cybercat.gigeresque.common.entity.ai.goals.movement;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class DodgeProjectilesGoal extends Goal {

    protected final AlienEntity alienEntity;

    private static final double dodgeChance = 0.15D;

    private Vec3 projectileMotionDirection;

    private int giveUpDelay;

    private int dodgeDelay;

    public DodgeProjectilesGoal(AlienEntity alienEntity) {
        this.alienEntity = alienEntity;
        setFlags(EnumSet.of(Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        return dodgeDelay-- <= 0 && projectileMotionDirection != null && giveUpDelay-- > 0 && alienEntity.onGround() && !alienEntity
            .isPassenger();
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void start() {
        if (projectileMotionDirection != null) {
            var selfAxis = new Vec3(0.0, 1.0, 0.0);
            var dodgeDirection = selfAxis.cross(projectileMotionDirection);

            var velocity = 0.8;
            if (alienEntity.getRandom().nextBoolean()) {
                velocity = -velocity;
            }

            alienEntity.setDeltaMovement(dodgeDirection.x * velocity, 0.3, dodgeDirection.z * velocity);

            setDodgeTarget(null);
            dodgeDelay = 40;
        }
    }

    private void setDodgeTarget(@Nullable Vec3 projectileDirection) {
        if (projectileDirection == null) {
            projectileMotionDirection = null;
            giveUpDelay = 0;
        } else if (dodgeDelay <= 0 && alienEntity.getRandom().nextDouble() < dodgeChance) {
            projectileMotionDirection = projectileDirection;
            giveUpDelay = 10;
        }
    }

    private static void tryDodgeProjectile(PathfinderMob entity, Vec3 projectileDirection) {
        for (var task : new ArrayList<>(entity.goalSelector.getAvailableGoals())) {
            if (task.getGoal() instanceof DodgeProjectilesGoal dodgeProjectilesGoal) {
                dodgeProjectilesGoal.setDodgeTarget(projectileDirection);
            }
        }
    }

    public static void doDodgeCheckForProjectile(Entity projectile) {
        if (!(projectile.level() instanceof ServerLevel level)) {
            return;
        }

        if (projectile.onGround()) {
            return;
        }

        if (projectile instanceof AbstractArrow arrow && arrow.inGround) {
            return;
        }

        var width = projectile.getBbWidth() + 0.3F;
        final var projectileMotion = projectile.getDeltaMovement();
        final var vH = Math.sqrt(projectileMotion.x * projectileMotion.x + projectileMotion.z * projectileMotion.z);
        final var projectileDirection = new Vec3(projectileMotion.x / vH, 0.0, projectileMotion.z / vH);

        final var rangeVertical = 16;
        final var rangeHorizontal = 24;
        for (var entity : level.getAllEntities()) {
            if (entity instanceof PathfinderMob pathfinderMob) {
                final var distanceY = Math.abs((int) entity.position().y - (int) projectile.position().y);
                if (distanceY <= rangeVertical) {
                    final var distanceX = entity.position().x - projectile.position().x;
                    final var distanceZ = entity.position().z - projectile.position().z;
                    final var distanceH = Math.sqrt(distanceX * distanceX + distanceZ * distanceZ);
                    if (distanceH <= rangeHorizontal) {
                        final var cos = (projectileDirection.x * distanceX + projectileDirection.z * distanceZ) / distanceH;
                        final var sin = Math.sqrt(1 - cos * cos);
                        if (width > distanceH * sin) {
                            tryDodgeProjectile(pathfinderMob, projectileDirection);
                        }
                    }
                }
            }
        }
    }
}
