package mods.cybercat.gigeresque.common.entity.ai.goals.attack;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class LungeAtTargetFaceGoal extends Goal {

    private static final int DEFAULT_WIND_UP_TIME_IN_TICKS = 10;

    private static final float DEFAULT_DISTANCE_TARGET = -1F;

    private final FacehuggerEntity mob;

    private final int maxCooldown;

    private final float normalizedChance;

    private final double minLungeRange = 1;

    private final double maxLungeRange;

    private float distanceToTarget;

    private int windUpTimeInTicks;

    private int cooldown;

    @Nullable
    Runnable onLungeCallback;

    public LungeAtTargetFaceGoal(FacehuggerEntity mob, float normalizedChance, int cooldown, double maxLungeRange) {
        this.mob = mob;
        this.maxCooldown = cooldown;
        this.normalizedChance = normalizedChance;
        this.maxLungeRange = maxLungeRange;
        this.cooldown = 0;

        resetWindUpTimeInTicks();
        distanceToTarget = DEFAULT_DISTANCE_TARGET;

        setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        cooldown = Math.max(cooldown - 1, 0);

        var canUse = !isOnCooldown() && mob.getRandom().nextFloat() < normalizedChance;

        if (this.mob.hasEffect(MobEffects.CONFUSION)) {
            return false;
        }

        if (this.mob.getTarget() != null && this.mob.getTarget().getType().is(GigTags.FACEHUGGER_BLACKLIST)) {
            return false;
        }

        if (this.mob.getTarget() != null && !GigEntityUtils.isTargetHostable(this.mob.getTarget())) {
            return false;
        }

        return mob.getTarget() != null &&
            mob.onGround() &&
            isInRange() &&
            canUse &&
            mob.getSensing().hasLineOfSight(mob.getTarget());
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
        return mob.getTarget() != null && mob.onGround() && mob.getSensing().hasLineOfSight(mob.getTarget());
    }

    @Override
    public void start() {
        super.start();

        if (mob.getTarget() != null) {
            mob.getLookControl().setLookAt(mob.getTarget(), 180.0F, 180.0F);
        }
        mob.animationDispatcher.sendFacehuggerLunge();
    }

    @Override
    public void tick() {
        if (mob.getTarget() == null) {
            return;
        }

        var target = mob.getTarget();

        mob.getLookControl().setLookAt(target, 180.0F, 180.0F);

        var currentDistanceToTarget = mob.distanceTo(target);

        if (distanceToTarget == DEFAULT_DISTANCE_TARGET) {
            distanceToTarget = currentDistanceToTarget;
        }

        if (
            (mob.getLastHurtByMobTimestamp() > 0 && mob.tickCount - mob.getLastHurtByMobTimestamp() < 20) ||
                currentDistanceToTarget > distanceToTarget
        ) {
            windUpTimeInTicks = 0;
        }

        if (windUpTimeInTicks > 0) {
            windUpTimeInTicks--;
            mob.getNavigation().stop();
            return;
        }

        distanceToTarget = currentDistanceToTarget;

        var deltaMovement = mob.getDeltaMovement().scale(0.2);
        var vectorDifference = target.getEyePosition().subtract(mob.getEyePosition());

        vectorDifference = vectorDifference.normalize()
            .scale(0.2 * distanceToTarget)
            .add(deltaMovement.x, 0, deltaMovement.z);

        // 0.6 seems to be a good minimum value for lunging towards the target's upper half.
        mob.setDeltaMovement(vectorDifference.x, Math.max(0.6, vectorDifference.y), vectorDifference.z);

        if (onLungeCallback != null) {
            onLungeCallback.run();
        }

        resetWindUpTimeInTicks();
        resetCooldown();
    }

    @Override
    public void stop() {
        super.stop();
        resetCooldown();
        resetWindUpTimeInTicks();
        distanceToTarget = DEFAULT_DISTANCE_TARGET;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    private boolean isOnCooldown() {
        return cooldown > 0;
    }

    private void resetCooldown() {
        cooldown = maxCooldown;
    }

    private void resetWindUpTimeInTicks() {
        this.windUpTimeInTicks = DEFAULT_WIND_UP_TIME_IN_TICKS;
    }

    private boolean isInRange() {
        var target = mob.getTarget();

        if (target == null) {
            return false;
        }

        var distanceToHost = mob.distanceToSqr(target);

        var minimumRangeSquared = minLungeRange * minLungeRange;
        var maximumRangeSquared = maxLungeRange * maxLungeRange;

        return distanceToHost <= maximumRangeSquared && distanceToHost >= minimumRangeSquared;
    }
}
