package mods.cybercat.gigeresque.common.entity.helper.managers;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.nav.GigNodeEvaluator;

public class ClimbingManager {

    private final AlienEntity alien;

    public boolean canClimb;

    public float climbSpeedMultiplier;

    public final EntityDataAccessor<Boolean> isClimbingEDA;

    public boolean hasClosestCollision;

    public Vec3 closestCollision;

    public boolean climbing;

    // set in move control
    public boolean climbingRequiredForMovement;

    public ClimbingManager(AlienEntity alien, EntityDataAccessor<Boolean> isClimbingEDA) {
        this.alien = alien;
        this.isClimbingEDA = isClimbingEDA;
    }

    public void tick() {
        if (alien.level().isClientSide()) {
            climbing = alien.getEntityData().get(isClimbingEDA);
        } else {
            var alienBlockPos = alien.blockPosition();
            climbing = GigNodeEvaluator.climbable(
                alien.level(),
                alienBlockPos.getX(),
                alienBlockPos.getY(),
                alienBlockPos.getZ()
            )
                && (!alien.verticalCollisionBelow || climbingRequiredForMovement);
            alien.setNoGravity(climbing);

            closestCollision = getClosestBlockCollision(
                alien.level(),
                alien.position(),
                alien.getBbWidth() + 2,
                0.5
            );
            hasClosestCollision = closestCollision != null;

            if (climbing) {
                Vec3 pull;
                if (hasClosestCollision) {
                    pull = closestCollision.subtract(alien.center()).normalize();
                } else {
                    pull = Vec3.ZERO;
                }
                float pullSpeed = 1;
                float pullStrength = 0.2f;
                alien.setDeltaMovement(
                    alien.getDeltaMovement()
                        .scale(1.0 - pullStrength)
                        .add(pull.scale(pullStrength * pullSpeed))
                );
            }

            alien.getEntityData().set(isClimbingEDA, climbing);
            climbingRequiredForMovement = false;
        }
    }

    // borrowed from from another world 2
    // library mods are cool but ctrl+c and ctrl+v are cooler
    private static Vec3 getClosestBlockCollision(Level level, Vec3 pos, double range, double precision) {
        double currentPrecision = range * 2;

        var points = new ArrayList<Vec3>();
        points.add(pos);

        var pointsNext = new ArrayList<Vec3>();

        Vec3 closestSoFar = null;

        while (!points.isEmpty()) {
            double halfCurrentPrecision = currentPrecision / 2;
            boolean finalCheck = halfCurrentPrecision <= precision;

            double closestDistSqSoFar = Double.MAX_VALUE;

            for (var point : points) {
                var distSq = point.distanceToSqr(pos);

                if (distSq > closestDistSqSoFar)
                    continue;

                if (
                    level.noBlockCollision(
                        null,
                        new AABB(
                            point.x - halfCurrentPrecision,
                            point.y - halfCurrentPrecision,
                            point.z - halfCurrentPrecision,
                            point.x + halfCurrentPrecision,
                            point.y + halfCurrentPrecision,
                            point.z + halfCurrentPrecision
                        )
                    )
                )
                    continue;

                closestDistSqSoFar = distSq;
                closestSoFar = point;

                if (!finalCheck) {
                    var dist = currentPrecision / 3;
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            for (int k = -1; k < 2; k++) {
                                pointsNext.add(point.add(i * dist, j * dist, k * dist));
                            }
                        }
                    }
                }
            }

            points = pointsNext;
            pointsNext = new ArrayList<>();

            currentPrecision /= 3;
        }

        return closestSoFar;
    }

}
