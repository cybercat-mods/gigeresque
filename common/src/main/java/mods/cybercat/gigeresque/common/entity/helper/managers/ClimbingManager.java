package mods.cybercat.gigeresque.common.entity.helper.managers;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.nav.GigNodeEvaluator;

public class ClimbingManager {

    private final AlienEntity alien;

    public boolean canClimb;

    public float climbSpeedMultiplier = 1;

    public final EntityDataAccessor<Boolean> isClimbingEDA;

    public final EntityDataAccessor<Vector3f> forwardEDA;

    public final EntityDataAccessor<Vector3f> upEDA;

    public final EntityDataAccessor<Float> distFromBlockEDA;

    public boolean hasClosestCollision;

    public Vec3 closestCollision;

    public boolean climbing;

    // set in move control
    public boolean climbingRequiredForMovement;

    public float smoothing = 0.8f;

    public Vec3 up = new Vec3(0, 1, 0);

    public Vec3 oldUp = new Vec3(0, 1, 0);

    public Vec3 forward = new Vec3(1, 0, 0);

    public Vec3 oldForward = new Vec3(1, 0, 0);

    public float distFromBlock = 0;

    public float oldDistFromBlock = 0;

    public ClimbingManager(
        AlienEntity alien,
        EntityDataAccessor<Boolean> isClimbingEDA,
        EntityDataAccessor<Vector3f> forwardEDA,
        EntityDataAccessor<Vector3f> upEDA,
        EntityDataAccessor<Float> distFromBlockEDA
    ) {
        this.alien = alien;
        this.isClimbingEDA = isClimbingEDA;
        this.forwardEDA = forwardEDA;
        this.upEDA = upEDA;
        this.distFromBlockEDA = distFromBlockEDA;
    }

    public void tick() {
        if (!canClimb || alien.stasisManager.isStasis()) {
            climbing = false;
            return;
        }

        if (alien.level().isClientSide()) {
            climbing = alien.getEntityData().get(isClimbingEDA);
            {
                oldForward = forward;
                var forwardVector3f = alien.getEntityData().get(forwardEDA);
                forward = new Vec3(forwardVector3f.x, forwardVector3f.y, forwardVector3f.z)
                    .scale(1.0f - smoothing)
                    .add(oldForward.scale(smoothing));
            }
            {
                oldUp = up;
                var upVector3f = alien.getEntityData().get(upEDA);
                up = new Vec3(upVector3f.x, upVector3f.y, upVector3f.z)
                    .scale(1.0f - smoothing)
                    .add(oldUp.scale(smoothing));
            }
            {
                oldDistFromBlock = distFromBlock;
                distFromBlock = alien.getEntityData().get(distFromBlockEDA);
            }
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
                float dist = 0;
                if (hasClosestCollision) {
                    var offset = closestCollision.subtract(alien.center());
                    dist = (float) offset.length();
                    pull = offset.normalize();
                } else {
                    pull = Vec3.ZERO;
                }
                if (dist > 1) {
                    float pullSpeed = 1;
                    float pullStrength = 0.1f;
                    alien.setDeltaMovement(
                        alien.getDeltaMovement()
                            .scale(1.0 - pullStrength)
                            .add(pull.scale(pullStrength * pullSpeed))
                    );
                }
                distFromBlock = distFromBlock * smoothing + dist * (1.0f - smoothing);
            }

            if (hasClosestCollision) {
                up = alien.center().subtract(closestCollision).normalize();
                var vel = alien.getDeltaMovement();
                if (vel.lengthSqr() > 0.0001) {
                    forward = vel.normalize();
                }
            }

            if (forward.equals(Vec3.ZERO)) {
                forward = new Vec3(1, 0, 0);
            }

            // point forward at 90 degrees from up (prevents weird rotations sometimes)
            {
                var z = forward.cross(up);
                forward = up.cross(z).normalize();
            }

            alien.getEntityData().set(isClimbingEDA, climbing);
            alien.getEntityData()
                .set(
                    forwardEDA,
                    new Vector3f(
                        (float) forward.x,
                        (float) forward.y,
                        (float) forward.z
                    )
                );
            alien.getEntityData()
                .set(
                    upEDA,
                    new Vector3f(
                        (float) up.x,
                        (float) up.y,
                        (float) up.z
                    )
                );
            alien.getEntityData().set(distFromBlockEDA, distFromBlock);
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
