package mods.cybercat.gigeresque.common.entity.ai.goals.attack;

import mods.cybercat.gigeresque.bvanseg.Cooldown;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class DelayedAttackGoal extends MeleeAttackGoal {

    protected static final Predicate<BlockState> NEST = state -> state.is(GigBlocks.NEST_RESIN_WEB_CROSS.get());

    protected final AlienEntity alienEntity;

    protected final Cooldown attackAnimationCooldown;

    protected boolean ranAttackAnimation;

    public DelayedAttackGoal(AlienEntity alienEntity, double speedModifier, int delayTicksBeforeAttack) {
        super(alienEntity, speedModifier, true);
        this.alienEntity = alienEntity;
        this.attackAnimationCooldown = Cooldown.withCooldownTimeInTicks("attackAnimationCooldownInTicks", delayTicksBeforeAttack);
    }

    @Override
    public void stop() {
        var livingentity = alienEntity.getTarget();

        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
            alienEntity.setTarget(null);
        }

        alienEntity.setAggressive(false);
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

            mob.swing(InteractionHand.MAIN_HAND);
            mob.doHurtTarget(alienEntity.getTarget());

            this.ranAttackAnimation = false;
        }
    }

    @Override
    public boolean canUse() {
        return isAbleToAttack() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return isAbleToAttack() && super.canContinueToUse();
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity target) {
        if (!ranAttackAnimation && canPerformAttack(target)) {
            // Play the animation.
            alienEntity.animationSelector.select(alienEntity);
            this.ranAttackAnimation = true;
            // Reset the cooldown.
            attackAnimationCooldown.reset();
        }
    }

    protected boolean isAbleToAttack() {
        if (alienEntity.isVehicle() || alienEntity.getTarget() == null) {
            return false;
        }

        return !NEST.test(alienEntity.getTarget().getInBlockState());
    }
}
