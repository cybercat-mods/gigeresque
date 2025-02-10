package mods.cybercat.gigeresque.common.entity.ai.goals.attack;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.SplittableRandom;

import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;

public class ExplodeGoal extends DelayedAttackGoal {

    public ExplodeGoal(PopperEntity mob, double speedModifier, int delayTicksBeforeAttack) {
        super(mob, speedModifier, delayTicksBeforeAttack);
    }

    @Override
    public void checkAndPerformAttack(LivingEntity target) {
        if (this.canPerformAttack(target) && this.mob instanceof PopperEntity popperEntity) {
            if (this.delayBeforeAttack > 0) {
                this.delayBeforeAttack--;

                if (this.delayBeforeAttack == delayTicksBeforeAttack && !this.triggeredAttackAnimation) {
                    popperEntity.animationSelector.select(popperEntity);
                    this.triggeredAttackAnimation = true;
                }
            } else {
                var random = new SplittableRandom();
                var randomPhase = random.nextInt(0, 100);
                if (randomPhase >= 90) {
                    popperEntity.explode();
                    this.mob.remove(Entity.RemovalReason.KILLED);
                } else {
                    this.mob.doHurtTarget(target);
                    this.mob.swing(InteractionHand.MAIN_HAND);
                }
                this.resetAttackCooldown();
                this.triggeredAttackAnimation = false;
            }
        } else {
            this.delayBeforeAttack = this.adjustedTickDelay(10);
            this.triggeredAttackAnimation = false;
        }
    }
}
