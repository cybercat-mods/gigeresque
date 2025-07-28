package mods.cybercat.gigeresque.common.entity.ai.goals.attack;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;

import java.util.SplittableRandom;

import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;

public class ExplodeGoal extends DelayedAttackGoal {

    public ExplodeGoal(PopperEntity mob, double speedModifier, int delayTicksBeforeAttack) {
        super(mob, speedModifier, delayTicksBeforeAttack);
    }

    @Override
    public void tick() {
        super.tick();
        attackAnimationCooldown.tick();

        if (
            // If target is not null
            alienEntity.getTarget() != null
                // AND we ran the attack animation.
                && ranAttackAnimation
                // AND the animation cooldown has finished
                && !attackAnimationCooldown.isActive()
                // AND the target is still within melee range
                && alienEntity.isWithinMeleeAttackRange(alienEntity.getTarget())
                // AND we still have a line of sight of the target
                && alienEntity.getSensing().hasLineOfSight(alienEntity.getTarget())
        ) {
            resetAttackCooldown();
            var random = new SplittableRandom();
            var randomPhase = random.nextInt(0, 100);
            if (randomPhase >= 90) {
                if (alienEntity instanceof PopperEntity popperEntity) {
                    popperEntity.explode();
                }
                this.mob.remove(Entity.RemovalReason.KILLED);
            } else {
                this.mob.doHurtTarget(alienEntity.getTarget());
                this.mob.swing(InteractionHand.MAIN_HAND);
            }

            this.ranAttackAnimation = false;
        }
    }
}
