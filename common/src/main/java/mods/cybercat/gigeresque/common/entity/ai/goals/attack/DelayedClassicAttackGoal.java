package mods.cybercat.gigeresque.common.entity.ai.goals.attack;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;
import java.util.function.Predicate;
import java.util.stream.Stream;

import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class DelayedClassicAttackGoal extends MeleeAttackGoal {

    private int delayBeforeAttack;

    private boolean triggeredAttackAnimation;

    private final int delayTicksBeforeAttack;

    public static final Predicate<BlockState> NEST = state -> state.is(GigBlocks.NEST_RESIN_WEB_CROSS.get());

    public DelayedClassicAttackGoal(AlienEntity mob, double speedModifier, int delayTicksBeforeAttack) {
        super(mob, speedModifier, true);
        this.delayTicksBeforeAttack = delayTicksBeforeAttack;
    }

    @Override
    public boolean canUse() {
        return !this.mob.isVehicle() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.isVehicle() && super.canContinueToUse();
    }

    @Override
    public void start() {
        super.start();
        this.delayBeforeAttack = 0;
        this.triggeredAttackAnimation = false;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target) {
        var nearbyBlocks = mob.level().getBlockStatesIfLoaded(mob.getBoundingBox().inflate(18.0));
        var randomPhase = mob.getRandom().nextInt(0, 100);
        if (this.canPerformAttack(target) && this.mob instanceof AlienEntity mob) {
            if (this.mob.isVehicle()) {
                return;
            }

            if (this.delayBeforeAttack > 0) {
                this.delayBeforeAttack--;

                if (this.delayBeforeAttack == delayTicksBeforeAttack && !this.triggeredAttackAnimation) {
                    mob.animationSelector.select(mob);
                    this.triggeredAttackAnimation = true;
                }
            } else {
                if (isTargetValidForExecution(target)) {
                    if (shouldNestBehavior(nearbyBlocks, randomPhase) && GigEntityUtils.isTargetHostable(target)) {
                        mob.grabTarget(target);
                    } else if (shouldBiteBehavior(target, randomPhase)) {
                        mob.grabTarget(target);
                        mob.setIsBiting(true);
                    } else if (!mob.isVehicle()) {
                        this.mob.swing(InteractionHand.MAIN_HAND);
                        this.mob.doHurtTarget(target);
                    }
                }
                this.resetAttackCooldown();
                this.triggeredAttackAnimation = false;
            }
        } else {
            this.delayBeforeAttack = this.adjustedTickDelay(10);
            this.triggeredAttackAnimation = false;
        }
    }

    private boolean isTargetValidForExecution(LivingEntity target) {
        return !target.getType().is(GigTags.XENO_EXECUTE_BLACKLIST);
    }

    private boolean shouldNestBehavior(Stream<BlockState> nearbyBlocks, int randomPhase) {
        return nearbyBlocks.anyMatch(NEST) && randomPhase >= 50;
    }

    private boolean shouldBiteBehavior(LivingEntity target, int randomPhase) {
        return target.getHealth() <= (target.getMaxHealth() * 0.50) && randomPhase >= 80;
    }
}
