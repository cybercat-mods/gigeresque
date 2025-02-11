package mods.cybercat.gigeresque.common.entity.ai.goals.attack;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class DelayedAttackGoal extends MeleeAttackGoal {

    int delayBeforeAttack;

    boolean triggeredAttackAnimation;

    protected final int delayTicksBeforeAttack;

    public DelayedAttackGoal(AlienEntity mob, double speedModifier, int delayTicksBeforeAttack) {
        super(mob, speedModifier, true);
        this.delayTicksBeforeAttack = delayTicksBeforeAttack;
    }

    @Override
    public boolean canUse() {
        if (this.mob.isVehicle()) {
            return false;
        }

        if (this.mob.getTarget() != null && this.mob.getTarget().getInBlockState().is(GigBlocks.NEST_RESIN_WEB_CROSS.get())) {
            return false;
        }

        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.isVehicle()) {
            return false;
        }

        if (this.mob.getTarget() != null && this.mob.getTarget().getInBlockState().is(GigBlocks.NEST_RESIN_WEB_CROSS.get())) {
            return false;
        }

        return super.canContinueToUse();
    }

    @Override
    public void start() {
        super.start();
        this.delayBeforeAttack = 0;
        this.triggeredAttackAnimation = false;
    }

    @Override
    public void checkAndPerformAttack(LivingEntity target) {
        if (this.canPerformAttack(target) && this.mob instanceof AlienEntity mob) {
            if (this.delayBeforeAttack > 0) {
                this.delayBeforeAttack--;

                if (this.delayBeforeAttack == delayTicksBeforeAttack && !this.triggeredAttackAnimation) {
                    mob.animationSelector.select(mob);
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
}
