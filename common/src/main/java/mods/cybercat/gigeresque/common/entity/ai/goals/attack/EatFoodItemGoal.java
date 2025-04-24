package mods.cybercat.gigeresque.common.entity.ai.goals.attack;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.EnumSet;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;

public class EatFoodItemGoal extends Goal {

    protected final AlienEntity mob;

    private final double speedModifier;

    private double pathedTargetX;

    private double pathedTargetY;

    private double pathedTargetZ;

    private int ticksUntilNextPathRecalculation;

    private int ticksUntilNextAttack;

    int delayBeforeAttack;

    boolean triggeredAttackAnimation;

    protected final int delayTicksBeforeAttack;

    private long lastCanUseCheck;

    private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;

    public EatFoodItemGoal(AlienEntity mob, double speedModifier, int delayTicksBeforeAttack) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.delayTicksBeforeAttack = delayTicksBeforeAttack;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (mob.isBirthed()) {
            return false;
        }
        var gameTime = this.mob.level().getGameTime();
        if (gameTime - this.lastCanUseCheck < COOLDOWN_BETWEEN_CAN_USE_CHECKS) {
            return false;
        } else {
            this.lastCanUseCheck = gameTime;
            return this.mob.level()
                .getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(15))
                .stream()
                .anyMatch(entity -> {
                    var itemStack = entity.getItem();
                    return itemStack.is(GigTags.BURSTER_FOODS) && entity.isAlive() && !entity.hasPickUpDelay();
                });
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (mob.isBirthed()) {
            return false;
        }
        return this.mob.level().getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(15)).stream().anyMatch(entity -> {
            var itemStack = entity.getItem();
            return itemStack.is(GigTags.BURSTER_FOODS) && entity.isAlive() && !entity.hasPickUpDelay();
        });
    }

    @Override
    public void start() {
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
        this.delayBeforeAttack = 0;
        this.triggeredAttackAnimation = false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        var target = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(15)).stream().findFirst();
        if (target.isPresent()) {
            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);

            if (
                this.ticksUntilNextPathRecalculation == 0
                    && (this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0
                        || target.get().distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0
                        || this.mob.getRandom().nextFloat() < 0.05F)
            ) {
                this.pathedTargetX = target.get().getX();
                this.pathedTargetY = target.get().getY();
                this.pathedTargetZ = target.get().getZ();
                this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                var d0 = this.mob.distanceToSqr(target.get());
                if (d0 > 1024.0) {
                    this.ticksUntilNextPathRecalculation += 10;
                } else if (d0 > 256.0) {
                    this.ticksUntilNextPathRecalculation += 5;
                }

                if (!this.mob.getNavigation().moveTo(target.get(), this.speedModifier)) {
                    this.ticksUntilNextPathRecalculation += 15;
                }

                this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
            }
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.mob.getNavigation().moveTo(target.get(), this.speedModifier);
        }
    }
}
