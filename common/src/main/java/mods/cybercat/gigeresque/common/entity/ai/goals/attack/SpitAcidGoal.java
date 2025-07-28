package mods.cybercat.gigeresque.common.entity.ai.goals.attack;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class SpitAcidGoal extends DelayedAttackGoal {

    public SpitAcidGoal(AlienEntity alienEntity, double speedModifier, int delayTicksBeforeAttack) {
        super(alienEntity, speedModifier, delayTicksBeforeAttack);
    }

    @Override
    protected boolean isAbleToAttack() {
        var livingentity = alienEntity.getTarget();

        if (alienEntity.isVehicle() || livingentity == null) {
            return false;
        }

        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
            return false;
        }

        return !NEST.test(livingentity.getInBlockState());
    }

    @Override
    public boolean canUse() {
        long i = this.mob.level().getGameTime();
        if (i - this.lastCanUseCheck < 20L) {
            return false;
        } else {
            this.lastCanUseCheck = i;
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                this.path = this.mob.getNavigation().createPath(livingentity, 0);
                return this.path != null || isAbleToAttack();
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && isAbleToAttack();
    }

    @Override
    public void tick() {
        super.tick();
        attackAnimationCooldown.tick();

        if (
            // AND we ran the attack animation.
            ranAttackAnimation
                // AND the animation cooldown has finished
                && !attackAnimationCooldown.isActive()
                // AND we still have a line of sight of the target
                && this.alienEntity.getSensing().hasLineOfSight(this.alienEntity.getTarget())
        ) {
            resetAttackCooldown();

            this.alienEntity.swing(InteractionHand.MAIN_HAND);
            if (this.alienEntity.isWithinMeleeAttackRange(this.alienEntity.getTarget())) {
                this.alienEntity.doHurtTarget(this.alienEntity.getTarget());
            } else {
                this.alienEntity.shootAcid(this.alienEntity.getTarget(), this.alienEntity);
            }

            this.ranAttackAnimation = false;
        }
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity target) {
        if (!ranAttackAnimation && canPerformAttack(target)) {
            // Play the animation.
            if (this.alienEntity.isWithinMeleeAttackRange(target)) {
                this.alienEntity.animationSelector.select(alienEntity);
            } else {
                this.alienEntity.animationDispatcher.sendAcidSpit();
            }
            this.ranAttackAnimation = true;
            // Reset the cooldown.
            attackAnimationCooldown.reset();
        }
    }

    @Override
    protected boolean canPerformAttack(@NotNull LivingEntity entity) {
        return this.isTimeToAttack() && this.alienEntity.getSensing().hasLineOfSight(entity);
    }
}
