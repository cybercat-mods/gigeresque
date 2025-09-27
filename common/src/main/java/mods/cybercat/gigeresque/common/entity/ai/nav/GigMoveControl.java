package mods.cybercat.gigeresque.common.entity.ai.nav;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

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
                alien.climbingManager.climbingRequiredForMovement = true;
            }
        }

        if (alien.climbingManager.climbing) {
            tickClimbing();
        } else {
            super.tick();
        }
    }

    private void tickClimbing() {
        if (operation != Operation.MOVE_TO) {
            return;
        }
        var wanted = new Vec3(wantedX, wantedY + 0.5, wantedZ);
        operation = Operation.WAIT;
        var offset = wanted.subtract(alien.center());
        var speed = alien.getAttributeValue(Attributes.MOVEMENT_SPEED) * speedModifier * alien.climbingManager.climbSpeedMultiplier;
        var dist = offset.length();
        if (speed > dist) {
            speed = dist;
        }
        if (dist < 0.1f) {
            alien.setZza(0);
            return;
        }
        var desiredVelocity = offset.normalize().scale(speed);
        alien.setDeltaMovement(desiredVelocity);
    }

}
