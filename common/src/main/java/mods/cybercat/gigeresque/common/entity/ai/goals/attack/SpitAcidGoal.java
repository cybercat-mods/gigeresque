package mods.cybercat.gigeresque.common.entity.ai.goals.attack;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class SpitAcidGoal extends Goal {

    protected final AlienEntity mob;

    private final double speedModifier;

    private Path path;

    private int ticksUntilNextAttack;

    int delayBeforeAttack;

    boolean triggeredAttackAnimation;

    private final int attackInterval = 80;

    protected final int delayTicksBeforeAttack;

    private long lastCanUseCheck;

    private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;

    private boolean strafingClockwise;

    private boolean strafingBackwards;

    private int strafingTime = -1;

    private int seeTime;

    private final float attackRadiusSqr;

    public SpitAcidGoal(AlienEntity mob, double speedModifier, int delayTicksBeforeAttack) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.delayTicksBeforeAttack = delayTicksBeforeAttack;
        this.attackRadiusSqr = 50;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        long i = this.mob.level().getGameTime();
        if (i - this.lastCanUseCheck < COOLDOWN_BETWEEN_CAN_USE_CHECKS) {
            return false;
        } else {
            this.lastCanUseCheck = i;
            var target = this.mob.getTarget();
            if (target == null || !target.isAlive()) {
                return false;
            } else {
                this.path = this.mob.getNavigation().createPath(target, 0);
                return this.path != null || !this.mob.isWithinMeleeAttackRange(target);
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        var target = this.mob.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        } else {
            return this.mob.isWithinRestriction(target.blockPosition()) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target);
        }
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.path, this.speedModifier);
        this.mob.setAggressive(true);
        this.ticksUntilNextAttack = 0;
        this.delayBeforeAttack = 0;
        this.triggeredAttackAnimation = false;
    }

    @Override
    public void stop() {
        var livingentity = this.mob.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
            this.mob.setTarget(null);
        }
        this.seeTime = 0;

        this.mob.setAggressive(false);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        var target = this.mob.getTarget();
        if (target != null) {
            double d0 = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
            boolean flag = this.mob.getSensing().hasLineOfSight(target);
            boolean flag1 = this.seeTime > 0;
            if (flag != flag1) {
                this.seeTime = 0;
            }

            if (flag) {
                this.seeTime++;
            } else {
                this.seeTime--;
            }

            if (d0 <= this.attackRadiusSqr && this.seeTime >= 20) {
                this.strafingTime++;
            } else {
                this.mob.getNavigation().moveTo(target, this.speedModifier);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if (this.mob.getRandom().nextFloat() < 0.3) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if (this.mob.getRandom().nextFloat() < 0.3) {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (d0 > (this.attackRadiusSqr * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (d0 < (this.attackRadiusSqr * 0.25F)) {
                    this.strafingBackwards = true;
                }

                this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                if (this.mob.getControlledVehicle() instanceof Mob mobVehicle) {
                    mobVehicle.lookAt(target, 30.0F, 30.0F);
                }

                this.mob.lookAt(target, 30.0F, 30.0F);
            } else {
                this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }

            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);

            if (this.mob.isWithinMeleeAttackRange(target)) {
                this.checkAndPerformAttackMelee(target);
            } else {
                this.checkAndPerformAttack(target);
            }

        }
    }

    protected void checkAndPerformAttackMelee(LivingEntity target) {
        if (this.canPerformAttack(target) && this.mob.isWithinMeleeAttackRange(target)) {
            if (this.delayBeforeAttack > 0) {
                this.delayBeforeAttack--;

                if (this.delayBeforeAttack == delayTicksBeforeAttack && !this.triggeredAttackAnimation) {
                    mob.animationSelector.select(this.mob);
                    this.triggeredAttackAnimation = true;
                }
            } else {
                this.resetAttackCooldown();
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(target);
                this.triggeredAttackAnimation = false;
            }
        } else {
            this.delayBeforeAttack = this.adjustedTickDelay(10);
            this.triggeredAttackAnimation = false;
        }
    }

    protected void checkAndPerformAttack(LivingEntity target) {
        if (this.canPerformAttack(target)) {
            if (this.delayBeforeAttack > 0) {
                this.delayBeforeAttack--;

                if (this.delayBeforeAttack == delayTicksBeforeAttack && !this.triggeredAttackAnimation) {
                    this.mob.animationDispatcher.sendAcidSpit();
                    this.triggeredAttackAnimation = true;
                }
            } else {
                this.resetAttackCooldown();
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.shootAcid(target, this.mob);
                this.triggeredAttackAnimation = false;
            }
        } else {
            this.delayBeforeAttack = this.adjustedTickDelay(10);
            this.triggeredAttackAnimation = false;
        }
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(this.attackInterval);
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected boolean canPerformAttack(LivingEntity entity) {
        return this.isTimeToAttack() && !this.mob.isWithinMeleeAttackRange(entity) && this.mob.getSensing().hasLineOfSight(entity);
    }
}
