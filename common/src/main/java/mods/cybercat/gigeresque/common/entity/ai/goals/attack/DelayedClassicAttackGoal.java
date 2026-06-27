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

public class DelayedClassicAttackGoal extends MeleeAttackGoal {

    private static final Predicate<BlockState> NEST = state -> state.is(GigBlocks.NEST_RESIN_WEB_CROSS.get());

    private final AlienEntity alienEntity;

    private final Cooldown attackAnimationCooldown;

    private boolean ranAttackAnimation;

    public DelayedClassicAttackGoal(AlienEntity alienEntity, double speedModifier, int delayTicksBeforeAttack) {
        super(alienEntity, speedModifier, true);
        this.alienEntity = alienEntity;
        this.attackAnimationCooldown = Cooldown.withCooldownTimeInTicks("attackAnimationCooldownInTicks", delayTicksBeforeAttack);
    }

    @Override
    public void tick() {
        super.tick();
        attackAnimationCooldown.tick();
        if (alienEntity.isVehicle()) {
            alienEntity.setTarget(null);
            alienEntity.setAggressive(false);
            return;
        }

        super.tick();

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

            if (canGrab()) {
                alienEntity.grabTarget(alienEntity.getTarget());
            } else {
                mob.swing(InteractionHand.MAIN_HAND);
                mob.doHurtTarget(alienEntity.getTarget());
            }

            this.ranAttackAnimation = false;
        }
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

    @Override
    public boolean canUse() {
        return isAbleToAttack() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return isAbleToAttack() && super.canContinueToUse();
    }

    private boolean isAbleToAttack() {
        if (alienEntity.isVehicle() || alienEntity.getTarget() == null) {
            return false;
        }
        if (alienEntity.hasEffect(MobEffects.CONFUSION)) {
            return false;
        }
        if (NEST.test(alienEntity.getTarget().getInBlockState())) {
            return false;
        }
        if (alienEntity.getTarget().getVehicle() instanceof AlienEntity) {
            alienEntity.setTarget(null);
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
    public void stop() {
        var livingentity = alienEntity.getTarget();

        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
            alienEntity.setTarget(null);
        }

        alienEntity.setAggressive(false);
    }

    private boolean canGrab() {
        if (mob.getTarget() instanceof LivingEntity livingEntity) {
            var randomPhase = mob.getRandom().nextInt(0, 100);
            return randomPhase < 33 && livingEntity.getHealth() <= (livingEntity.getMaxHealth() * 0.50);
        }

        return false;
    }
}
