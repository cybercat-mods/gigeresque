package mods.cybercat.gigeresque.common.entity.ai.goals.attack;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

/**
 * Credit to Boston/AVP
 */
public class LungeAtFoodTargetGoal extends Goal {

    private static final int DEFAULT_WIND_UP_TIME_IN_TICKS = 10;

    private static final float DEFAULT_DISTANCE_TARGET = -1F;

    private final Mob mob;

    private final int maxCooldown;

    private final float normalizedChance;

    private final double minLungeRange = 1;

    private final double maxLungeRange;

    private float distanceToTarget;

    private int windUpTimeInTicks;

    private int cooldown;

    @Nullable
    Runnable onLungeCallback;

    public LungeAtFoodTargetGoal(Mob mob, float normalizedChance, int cooldown, double maxLungeRange) {
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

        var target = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(5)).stream().findFirst();

        return target.isPresent() &&
            mob.onGround() &&
            isInRange() &&
            canUse &&
            mob.getSensing().hasLineOfSight(target.get());
    }

    @Override
    public boolean canContinueToUse() {
        var target = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(5)).stream().findFirst();

        return target.isPresent() && mob.onGround() && mob.getSensing().hasLineOfSight(target.get());
    }

    @Override
    public void start() {
        super.start();

        var target = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(5)).stream().findFirst();

        if (target != null) {
            mob.getLookControl().setLookAt(target.get(), 180.0F, 180.0F);
        }
    }

    @Override
    public void tick() {
        var target = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(5)).stream().findFirst();

        if (target.isEmpty()) {
            return;
        }

        mob.getLookControl().setLookAt(target.get(), 180.0F, 180.0F);

        var currentDistanceToTarget = mob.distanceTo(target.get());

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
        var vectorDifference = target.get().getEyePosition().subtract(mob.getEyePosition());

        vectorDifference = vectorDifference.normalize()
            .scale(0.2 * distanceToTarget)
            .add(deltaMovement.x, 0, deltaMovement.z);

        // 0.6 seems to be a good minimum value for lunging towards the target's upper half.
        mob.setDeltaMovement(vectorDifference.x, Math.max(0.6, vectorDifference.y), vectorDifference.z);

        if (mob instanceof AlienEntity alienEntity) {
            alienEntity.animationDispatcher.sendSwim();
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

        if (mob instanceof AlienEntity alienEntity) {
            alienEntity.animationDispatcher.sendIdle();
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public LungeAtFoodTargetGoal setOnLungeCallback(@Nullable Runnable onLungeCallback) {
        this.onLungeCallback = onLungeCallback;
        return this;
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
        var target = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(5)).stream().findFirst();

        if (target.isEmpty()) {
            return false;
        }

        var distanceToHost = mob.distanceToSqr(target.get());

        var minimumRangeSquared = minLungeRange * minLungeRange;
        var maximumRangeSquared = maxLungeRange * maxLungeRange;

        return distanceToHost <= maximumRangeSquared && distanceToHost >= minimumRangeSquared;
    }
}
