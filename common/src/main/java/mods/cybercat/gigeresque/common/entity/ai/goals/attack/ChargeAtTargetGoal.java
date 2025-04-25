package mods.cybercat.gigeresque.common.entity.ai.goals.attack;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class ChargeAtTargetGoal extends Goal {

    private enum Activity {
        NONE,
        CHARGE_UP,
        CHARGING,
        STUNNED
    }

    private final AlienEntity alienEntity;

    private Activity currentActivity = Activity.NONE;

    private int attackTime;

    private Vec3 attackVec;

    public ChargeAtTargetGoal(AlienEntity alienEntity) {
        this.alienEntity = alienEntity;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (--attackTime > 0 || !alienEntity.onGround() || alienEntity.isPassenger()) {
            return false;
        }

        final var target = alienEntity.getTarget();
        if (target != null) {
            final var distanceSqr = alienEntity.distanceToSqr(target);
            return distanceSqr >= 3 && alienEntity.hasLineOfSight(target);
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return currentActivity != Activity.NONE;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        alienEntity.getNavigation().stop();
        attackTime = 20;
        currentActivity = Activity.CHARGE_UP;
        alienEntity.swing(InteractionHand.OFF_HAND);
        alienEntity.setDeltaMovement(alienEntity.getDeltaMovement().add(0.0, 0.3, 0.0));
        alienEntity.playSound(
            SoundEvents.ARMOR_EQUIP_LEATHER.value(),
            1.0F,
            1.0F / (alienEntity.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        alienEntity.getNavigation().stop();
    }

    @Override
    public void stop() {
        attackTime = alienEntity.getRandom().nextInt(20, 80);
        cancelCharging();
    }

    @Override
    public void tick() {
        attackTime--;
        switch (currentActivity) {
            case CHARGE_UP:
                tickChargeUp();
                break;
            case CHARGING:
                tickCharging();
                break;
            case STUNNED:
                tickStunned();
                break;
            default:
        }
    }

    private void tickChargeUp() {
        final var target = alienEntity.getTarget();
        if (target == null || !target.isAlive()) {
            currentActivity = Activity.NONE;
            return;
        }

        if (alienEntity.level().isClientSide()) {
            alienEntity.animationDispatcher.sendRun();
        }

        alienEntity.getLookControl().setLookAt(target, 100.0F, 100.0F);

        if (attackTime <= 0) {
            attackVec = new Vec3(target.getX() - alienEntity.getX(), 0.0, target.getZ() - alienEntity.getZ()).normalize();
            alienEntity.setSprinting(true);

            attackTime = 20;
            currentActivity = Activity.CHARGING;
        }
    }

    private void tickCharging() {
        final var target = alienEntity.getTarget();

        if (alienEntity.level().isClientSide()) {
            alienEntity.animationDispatcher.sendRun();
        }
        alienEntity.lookAt(EntityAnchorArgument.Anchor.FEET, alienEntity.position().add(attackVec));
        alienEntity.setDeltaMovement(
            attackVec.x * 2.0F,
            alienEntity.getDeltaMovement().y,
            attackVec.z * 2.0F
        );
        final boolean hit;
        if (target != null) {
            var list = alienEntity.level().getEntities(alienEntity, alienEntity.getBoundingBox().inflate(0.2));
            hit = list.contains(target);
        } else {
            hit = false;
        }

        if (hit) {
            alienEntity.swing(InteractionHand.MAIN_HAND);

            alienEntity.setDeltaMovement(
                attackVec.x * -1.2,
                0.4,
                attackVec.z * -1.2
            );
            target.setDeltaMovement(
                attackVec.x * 3.0F,
                0.5,
                attackVec.z * 3.0F
            );
            alienEntity.doHurtTarget(target);
            currentActivity = Activity.NONE;
        } else if (alienEntity.horizontalCollision) {
            alienEntity.hurt(alienEntity.level().damageSources().flyIntoWall(), 5.0F);
            alienEntity.setDeltaMovement(
                attackVec.x * -0.5,
                0.5,
                attackVec.z * -0.5
            );
            cancelCharging();
            attackTime = 60;
            currentActivity = Activity.STUNNED;
        } else if (attackTime <= 0 || alienEntity.isInWater() || alienEntity.isInLava()) {
            currentActivity = Activity.NONE;
        }
    }

    private void tickStunned() {
        if (attackTime <= 0) {
            currentActivity = Activity.NONE;
        }
    }

    private void cancelCharging() {
        alienEntity.setSprinting(false);
    }
}
