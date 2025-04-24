package mods.cybercat.gigeresque.common.entity.ai.goals.attack;

import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;

public class FacehuggerRunToTargetGoal extends DelayedAttackGoal {

    public FacehuggerRunToTargetGoal(AlienEntity mob, double speedModifier, int delayTicksBeforeAttack) {
        super(mob, speedModifier, delayTicksBeforeAttack);
    }

    @Override
    public boolean canUse() {
        if (this.mob.hasEffect(MobEffects.CONFUSION)) {
            return false;
        }

        if (this.mob.getTarget() != null && this.mob.getTarget().getType().is(GigTags.FACEHUGGER_BLACKLIST)) {
            return false;
        }

        if (this.mob.getTarget() != null && !GigEntityUtils.isTargetHostable(this.mob.getTarget())) {
            return false;
        }

        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.hasEffect(MobEffects.CONFUSION)) {
            return false;
        }

        if (this.mob.getTarget() != null && this.mob.getTarget().getType().is(GigTags.FACEHUGGER_BLACKLIST)) {
            return false;
        }

        if (this.mob.getTarget() != null && !GigEntityUtils.isTargetHostable(this.mob.getTarget())) {
            return false;
        }

        return super.canContinueToUse();
    }

    @Override
    public void checkAndPerformAttack(LivingEntity target) {
        if (this.canPerformAttack(target) && this.mob instanceof FacehuggerEntity mob) {
            if (this.delayBeforeAttack > 0) {
                this.delayBeforeAttack--;

                if (this.delayBeforeAttack == delayTicksBeforeAttack && !this.triggeredAttackAnimation) {
                    this.triggeredAttackAnimation = true;
                }
            } else {
                this.resetAttackCooldown();
                this.triggeredAttackAnimation = false;
            }
        } else {
            this.delayBeforeAttack = this.adjustedTickDelay(10);
            this.triggeredAttackAnimation = false;
        }
    }
}
