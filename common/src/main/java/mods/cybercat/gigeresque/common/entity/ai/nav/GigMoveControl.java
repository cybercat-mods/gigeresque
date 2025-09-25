package mods.cybercat.gigeresque.common.entity.ai.nav;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import java.util.ArrayList;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class GigMoveControl extends MoveControl {

    public final AlienEntity alien;

    public GigMoveControl(AlienEntity alien) {
        super(alien);
        this.alien = alien;
    }

    @Override
    public void tick() {
        var blockPos = BlockPos.containing(alien.center());
        if (
            operation == Operation.MOVE_TO &&
                GigNodeEvaluator.climbable(
                    mob.level(),
                    blockPos.getX(),
                    blockPos.getY(),
                    blockPos.getZ()
                )
        ) {
            var belowTarget = BlockPos.containing(wantedX, wantedY + 0.5, wantedZ).below();
            var blockBelowTarget = alien.level().getBlockState(belowTarget);
            if (!blockBelowTarget.entityCanStandOn(alien.level(), belowTarget, alien)) {
                tickClimbing();
                return;
            }
        }

        super.tick();
    }

    private void tickClimbing() {
        if (operation != Operation.MOVE_TO) {
            return;
        }
        operation = Operation.WAIT;
        var offset = new Vec3(wantedX, wantedY + 0.5, wantedZ).subtract(alien.center());
        var speed = alien.getAttributeValue(Attributes.MOVEMENT_SPEED) * speedModifier * alien.climbSpeedMultiplier;
        var dist = offset.length();
        if (dist < 0.01f) {
            alien.setZza(0);
            return;
        }
        if (speed > dist) {
            speed = dist;
        }
        var desiredVelocity = offset.normalize().scale(speed);
        var closestCollision = getClosestBlockCollision(
            alien.level(),
            new Vector3d(alien.center().x, alien.center().y, alien.center().z),
            alien.getBbWidth() + 2,
            0.5
        );
        var pullTowardsSurface = new Vec3(
            closestCollision.x,
            closestCollision.y,
            closestCollision.z
        ).subtract(alien.center())
            .normalize()
            .scale(speed / 3);

        alien.setDeltaMovement(desiredVelocity.add(pullTowardsSurface));
    }

    // borrowed from from another world 2
    // library mods are cool but ctrl+c and ctrl+v are cooler
    public static Vector3d getClosestBlockCollision(Level level, Vector3d pos, double range, double precision) {
        double currentPrecision = range * 2;

        var points = new ArrayList<Vector3d>();
        points.add(pos);

        var pointsNext = new ArrayList<Vector3d>();

        Vector3d closestSoFar = null;

        while (!points.isEmpty()) {
            double halfCurrentPrecision = currentPrecision / 2;
            boolean finalCheck = halfCurrentPrecision <= precision;

            double closestDistSqSoFar = Double.MAX_VALUE;

            for (var point : points) {
                var distSq = point.distanceSquared(pos);

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
                if (closestSoFar == null) {
                    closestSoFar = new Vector3d(point);
                } else {
                    closestSoFar.set(point);
                }

                if (!finalCheck) {
                    var dist = currentPrecision / 3;
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            for (int k = -1; k < 2; k++) {
                                pointsNext.add(new Vector3d(point).add(i * dist, j * dist, k * dist));
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
