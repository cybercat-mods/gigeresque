package mods.cybercat.gigeresque.common.entity.helper.managers;

import net.minecraft.core.BlockPos;
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

    public Vec3 up = new Vec3(0, 1, 0);

    public Vec3 forward = new Vec3(1, 0, 0);

    public ClimbingManager(AlienEntity alien, EntityDataAccessor<Boolean> isClimbingEDA) {
        this.alien = alien;
        this.isClimbingEDA = isClimbingEDA;
    }

    public void tick() {
        if (alien.level().isClientSide()) {
            climbing = alien.getEntityData().get(isClimbingEDA);
        } else {
            var blockPos = BlockPos.containing(alien.center());
            climbing = GigNodeEvaluator.climbable(
                alien.level(),
                blockPos.getX(),
                blockPos.getY(),
                blockPos.getZ(),
                true
            )
                && climbingRequiredForMovement;

            // all this stuff is here instead of in GigMoveControl since it needs to update even when the mob isn't
            // moving
            alien.setNoGravity(climbing);

            closestCollision = getClosestBlockCollision(
                alien.level(),
                alien.center(),
                alien.getBbWidth() + 2,
                0.2
            );
            hasClosestCollision = closestCollision != null;

            if (climbing) {
                Vec3 pull;
                double distSq = 0;
                if (hasClosestCollision) {
                    var offset = closestCollision.subtract(alien.center());
                    distSq = offset.lengthSqr();
                    pull = offset.normalize();
                } else {
                    pull = Vec3.ZERO;
                }
                if (distSq > 1) {
                    float pullSpeed = 1;
                    float pullStrength = 0.1f;
                    alien.setDeltaMovement(
                        alien.getDeltaMovement()
                            .scale(1.0 - pullStrength)
                            .add(pull.scale(pullStrength * pullSpeed))
                    );
                }
            }

            if (hasClosestCollision) {
                up = alien.center().subtract(closestCollision).normalize();
                var vel = alien.getDeltaMovement();
                if (vel.lengthSqr() != 0) {
                    forward = vel.normalize();
                }
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
