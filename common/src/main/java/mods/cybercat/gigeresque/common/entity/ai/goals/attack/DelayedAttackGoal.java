package mods.cybercat.gigeresque.common.entity.ai.goals.attack;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

import mods.cybercat.gigeresque.bvanseg.Cooldown;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;

public class DelayedAttackGoal extends MeleeAttackGoal {

    protected static final Predicate<BlockState> NEST = state -> state.is(GigBlocks.NEST_RESIN_WEB_CROSS.get());

    protected final AlienEntity alienEntity;

    protected final Cooldown attackAnimationCooldown;

    protected boolean ranAttackAnimation;

    protected int attackFrequency;

    public DelayedAttackGoal(AlienEntity alienEntity, double speedModifier, int delayTicksBeforeAttack, int attackFrequency) {
        super(alienEntity, speedModifier, true);
        this.alienEntity = alienEntity;
        this.attackAnimationCooldown = Cooldown.withCooldownTimeInTicks("attackAnimationCooldownInTicks", delayTicksBeforeAttack);
        this.attackFrequency = attackFrequency;
    }

    public DelayedAttackGoal(AlienEntity alienEntity, double speedModifier, int delayTicksBeforeAttack) {
        super(alienEntity, speedModifier, true);
        this.alienEntity = alienEntity;
        this.attackAnimationCooldown = Cooldown.withCooldownTimeInTicks("attackAnimationCooldownInTicks", delayTicksBeforeAttack);
        this.attackFrequency = 20;
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
        if (alienEntity.getTarget() != null) {
            if (
                alienEntity.getTarget()
                    .getPassengers()
                    .stream()
                    .anyMatch(e -> e instanceof FacehuggerEntity)
            ) {
                alienEntity.setTarget(null);
                alienEntity.setAggressive(false);
                return;
            }
            if (alienEntity.getTarget().hasEffect(GigStatusEffects.IMPREGNATION)) {
                alienEntity.setTarget(null);
                alienEntity.setAggressive(false);
                return;
            }
        }

        super.tick();
        attackAnimationCooldown.tick();

        if (
            alienEntity.getTarget() != null
                && ranAttackAnimation
                && !attackAnimationCooldown.isActive()
                && alienEntity.isWithinMeleeAttackRange(alienEntity.getTarget())
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
        if (alienEntity.hasEffect(MobEffects.CONFUSION)) {
            return false;
        }
        if (NEST.test(alienEntity.getTarget().getInBlockState())) {
            return false;
        }
        if (
            alienEntity.getTarget()
                .getPassengers()
                .stream()
                .anyMatch(e -> e instanceof FacehuggerEntity)
        ) {
            alienEntity.setTarget(null);
            return false;
        }
        if (alienEntity.getTarget().hasEffect(GigStatusEffects.IMPREGNATION)) {
            alienEntity.setTarget(null);
            return false;
        }
        return true;
    }

    @Override
    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(this.attackFrequency);
    }

}
