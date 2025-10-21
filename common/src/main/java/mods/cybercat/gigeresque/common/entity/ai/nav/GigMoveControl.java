package mods.cybercat.gigeresque.common.entity.ai.nav;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class GigMoveControl extends MoveControl {

    public final AlienEntity alien;

    public GigMoveControl(AlienEntity alien) {
        super(alien);
        this.alien = alien;
    }

    @Override
    public void tick() {
        alien.climbingManager.climbingRequiredForMovement = needsToClimb();

        if (alien.climbingManager.climbing) {
            tickClimbing();
        } else {
            super.tick();
        }
    }

    private void tickClimbing() {
        if (operation != Operation.MOVE_TO && operation != Operation.JUMPING) {
            return;
        }
        var wanted = new Vec3(wantedX, wantedY + 0.5, wantedZ);
        operation = Operation.WAIT;

        var offset = wanted.subtract(alien.center());
        var speed = alien.getAttributeValue(Attributes.MOVEMENT_SPEED)
            * speedModifier
            * alien.climbingManager.climbSpeedMultiplier;
        var dist = offset.length();
        if (speed > dist) {
            speed = dist;
        }
        if (dist < 0.1f) {
            alien.setZza(0);
            return;
        }

        {
            var desiredVelocity = offset.normalize().scale(speed);
            alien.setDeltaMovement(desiredVelocity);
        }

        if (CommonMod.config.generalConfigs.enableDevparticles) {
            ((ServerLevel) alien.level()).sendParticles(
                ParticleTypes.FLAME,
                wanted.x,
                wanted.y,
                wanted.z,
                1,
                0,
                0,
                0,
                0
            );
        }
    }

    private boolean needsToClimb() {
        var center = BlockPos.containing(alien.center());
        if (
            !GigNodeEvaluator.climbable(
                mob.level(),
                center.getX(),
                center.getY(),
                center.getZ(),
                true
            )
        ) {
            return false;
        }

        var currentMovementType = MovementType.requiredAt(alien, alien.blockPosition());
        if (currentMovementType == MovementType.CLIMB) {
            return true;
        }

        var wantedBlockPos = BlockPos.containing(wantedX, wantedY + 0.5, wantedZ);
        var wantedMovementType = MovementType.requiredAt(alien, wantedBlockPos);
        if (wantedMovementType == MovementType.CLIMB) {
            return true;
        }

        if (
            wantedBlockPos.equals(alien.blockPosition().above()) &&
                wantedMovementType == MovementType.JUMP
        ) {
            return true;
        }

        return false;
    }

    private enum MovementType {

        WALK,
        JUMP,
        CLIMB;

        private static MovementType requiredAt(AlienEntity alien, BlockPos pos) {
            var below = pos.below();
            var stateBelow = alien.level().getBlockState(below);
            if (stateBelow.entityCanStandOn(alien.level(), below, alien)) {
                return WALK;
            }
            var twoBelow = below.below();
            var stateTwoBelow = alien.level().getBlockState(twoBelow);
            if (stateTwoBelow.entityCanStandOn(alien.level(), twoBelow, alien)) {
                return JUMP;
            }
            return CLIMB;
        }
    }
}
