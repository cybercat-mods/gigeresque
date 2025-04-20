package mods.cybercat.gigeresque.common.entity.ai.goals.attack;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;

public class EatFoodItemGoal extends Goal {

    protected final AlienEntity mob;

    private final double speedModifier;

    private Path path;

    private double pathedTargetX;

    private double pathedTargetY;

    private double pathedTargetZ;

    private int ticksUntilNextPathRecalculation;

    private int ticksUntilNextAttack;

    int delayBeforeAttack;

    boolean triggeredAttackAnimation;

    private final int attackInterval = 20;

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
        long i = this.mob.level().getGameTime();
        if (i - this.lastCanUseCheck < COOLDOWN_BETWEEN_CAN_USE_CHECKS) {
            return false;
        } else {
            this.lastCanUseCheck = i;
            return this.mob.level()
                .getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(15))
                .stream()
                .anyMatch(entity -> {
                    ItemStack itemStack = entity.getItem();
                    return itemStack.is(GigTags.BURSTER_FOODS) && entity.isAlive() && !entity.hasPickUpDelay();
                });
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.level().getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(15)).stream().anyMatch(entity -> {
            ItemStack itemStack = entity.getItem();
            return itemStack.is(GigTags.BURSTER_FOODS) && entity.isAlive() && !entity.hasPickUpDelay() && isWithinMeleeAttackRange(entity);
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
        if (this.mob.level().getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(15)).stream().findFirst().isPresent()) {
            var target = this.mob.level()
                .getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(15))
                .stream()
                .findFirst()
                .get();
            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);

            if (
                this.ticksUntilNextPathRecalculation == 0
                    && (this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0
                        || target.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0
                        || this.mob.getRandom().nextFloat() < 0.05F)
            ) {
                this.pathedTargetX = target.getX();
                this.pathedTargetY = target.getY();
                this.pathedTargetZ = target.getZ();
                this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                var d0 = this.mob.distanceToSqr(target);
                if (d0 > 1024.0) {
                    this.ticksUntilNextPathRecalculation += 10;
                } else if (d0 > 256.0) {
                    this.ticksUntilNextPathRecalculation += 5;
                }

                if (!this.mob.getNavigation().moveTo(target, this.speedModifier)) {
                    this.ticksUntilNextPathRecalculation += 15;
                }

                this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
            }
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.mob.getNavigation().moveTo(target, this.speedModifier);
            this.checkAndPerformAttack(target);
        }
    }

    protected void checkAndPerformAttack(ItemEntity target) {
        if (this.canPerformAttack(target)) {
            if (this.delayBeforeAttack > 0) {
                this.delayBeforeAttack--;

                if (this.delayBeforeAttack == delayTicksBeforeAttack && !this.triggeredAttackAnimation) {
                    this.mob.animationDispatcher.sendChomp();
                    this.triggeredAttackAnimation = true;
                }
            } else {
                this.resetAttackCooldown();
                if (target.getItem().is(GigTags.POTIONS)) {
                    this.mob.playSound(SoundEvents.GLASS_BREAK, 1.0F, 1.0F);
                }
                target.getItem().finishUsingItem(this.mob.level(), this.mob);
                target.getItem().shrink(1);
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.setGrowth(this.mob.getGrowth() + 200.0F);
                this.triggeredAttackAnimation = false;
            }
        } else {
            this.delayBeforeAttack = this.adjustedTickDelay(10);
            this.triggeredAttackAnimation = false;
        }
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(this.attackInterval);
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected boolean canPerformAttack(ItemEntity entity) {
        return this.isTimeToAttack() && isWithinMeleeAttackRange(entity) && isBlockInViewAndReachable(entity, entity.blockPosition());
    }

    private boolean isBlockInViewAndReachable(ItemEntity entity, BlockPos blockPos) {
        var entityBlockPos = entity.blockPosition();

        if (entityBlockPos.equals(blockPos)) {
            return true;
        }

        return entityBlockPos.above().equals(blockPos);
    }

    public boolean isWithinMeleeAttackRange(@NotNull ItemEntity entity) {
        for (
            var testPos : BlockPos.betweenClosed(
                this.mob.blockPosition()
                    .relative(this.mob.getDirection(), 1)
                    .above(-1)
                    .relative(this.mob.getDirection().getClockWise(), -1),
                this.mob.blockPosition().relative(this.mob.getDirection(), 3).above(1).relative(this.mob.getDirection().getClockWise(), 1)
            )
        ) {
            if (entity.blockPosition().equals(testPos)) {
                return true;
            }
        }
        return this.mob.getBoundingBox().intersects(entity.getBoundingBox());
    }
}
