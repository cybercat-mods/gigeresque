package mods.cybercat.gigeresque.common.entity.ai.goals.nest;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.LightLayer;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.nest.NestBuildingHelper;

public class BuildNestGoal extends Goal {

    private long lastBuildTime = 0;

    int delayBeforeAttack;

    boolean triggeredAttackAnimation;

    protected final int delayTicksBeforeAttack = 5;

    protected final AlienEntity mob;

    private int ticksUntilNextAttack;

    private long lastCanUseCheck;

    public BuildNestGoal(AlienEntity mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        long i = this.mob.level().getGameTime();

        if (countNearbyAliens() >= 2) {
            if (i - this.lastCanUseCheck < 20L)
                return false;
            if (this.mob.getTarget() != null)
                return false;
            if (this.mob.getBlockStateOn().is(GigTags.DUNGEON_STAIRS))
                return false;
            if (this.mob.getBlockStateOn().is(GigTags.DUNGEON_BLOCKS))
                return false;
            if (this.mob.getBlockStateOn().is(GigTags.NEST_BLOCKS))
                return false;
            if (this.mob.isAggressive())
                return false;
            if (this.mob.crawlingManager.isCrawling())
                return false;
            if (this.mob.isVehicle())
                return false;
            if (this.mob.getGrowth() < this.mob.getMaxGrowth())
                return false;
            if (this.mob.level().canSeeSky(this.mob.blockPosition()))
                return false;
            if (this.mob.level().getBrightness(LightLayer.SKY, this.mob.blockPosition()) > 5)
                return false;
            if (this.mob.stasisManager.isStasis())
                return false;
            if (this.mob.isFleeing())
                return false;
            if (this.mob.level().dimensionType().piglinSafe())
                return false;
            this.lastCanUseCheck = i;
            return this.mob.isAlive();
        }
        if (i - lastBuildTime < 1200L)
            return false;
        if (this.mob.getTarget() != null)
            return false;
        if (i - this.lastCanUseCheck < 20L)
            return false;
        if (this.mob.getBlockStateOn().is(GigTags.DUNGEON_STAIRS))
            return false;
        if (this.mob.getBlockStateOn().is(GigTags.DUNGEON_BLOCKS))
            return false;
        if (this.mob.getBlockStateOn().is(GigTags.NEST_BLOCKS))
            return false;
        if (this.mob.isAggressive())
            return false;
        if (this.mob.crawlingManager.isCrawling())
            return false;
        if (this.mob.isVehicle())
            return false;
        if (this.mob.getGrowth() < this.mob.getMaxGrowth())
            return false;
        if (this.mob.level().canSeeSky(this.mob.blockPosition()))
            return false;
        if (this.mob.level().getBrightness(LightLayer.SKY, this.mob.blockPosition()) > 5)
            return false;
        if (this.mob.stasisManager.isStasis())
            return false;
        if (this.mob.isFleeing())
            return false;
        if (this.mob.level().dimensionType().piglinSafe())
            return false;
        this.lastCanUseCheck = i;
        return this.mob.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.getTarget() != null)
            return false;
        if (this.mob.getBlockStateOn().is(GigTags.DUNGEON_STAIRS))
            return false;
        if (this.mob.getBlockStateOn().is(GigTags.DUNGEON_BLOCKS))
            return false;
        if (this.mob.level().getBlockState(this.mob.blockPosition()).is(GigTags.DUNGEON_STAIRS))
            return false;
        if (this.mob.level().getBlockState(this.mob.blockPosition()).is(GigTags.DUNGEON_BLOCKS))
            return false;
        if (this.mob.level().getBlockState(this.mob.blockPosition().below()).is(GigTags.DUNGEON_STAIRS))
            return false;
        if (this.mob.level().getBlockState(this.mob.blockPosition().below()).is(GigTags.DUNGEON_BLOCKS))
            return false;
        if (this.mob.getBlockStateOn().is(GigTags.NEST_BLOCKS))
            return false;
        if (this.mob.isAggressive())
            return false;
        if (this.mob.crawlingManager.isCrawling())
            return false;
        if (this.mob.isVehicle())
            return false;
        if (this.mob.getGrowth() < this.mob.getMaxGrowth())
            return false;
        if (this.mob.level().canSeeSky(this.mob.blockPosition()))
            return false;
        if (this.mob.level().getBrightness(LightLayer.SKY, this.mob.blockPosition()) > 5)
            return false;
        if (this.mob.stasisManager.isStasis())
            return false;
        if (this.mob.isFleeing())
            return false;
        if (this.mob.level().dimensionType().piglinSafe())
            return false;
        return this.mob.isAlive();
    }

    @Override
    public void start() {
        this.delayBeforeAttack = 0;
        this.ticksUntilNextAttack = 0;
        this.triggeredAttackAnimation = false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        checkAndPerformNestPlacement();
    }

    protected void resetAttackCooldown() {
        lastBuildTime = this.mob.level().getGameTime();

        long nestBlocksNearby = BlockPos.betweenClosedStream(
            this.mob.blockPosition().offset(-5, -3, -5),
            this.mob.blockPosition().offset(5, 3, 5)
        )
            .filter(pos -> this.mob.level().getBlockState(pos).is(GigTags.NEST_BLOCKS))
            .count();

        int alienBonus = countNearbyAliens() * 50;
        int cooldown = (int) Math.max(100, Math.min(300 + nestBlocksNearby * 20 - alienBonus, 1200));
        this.ticksUntilNextAttack = this.adjustedTickDelay(cooldown);
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    public void checkAndPerformNestPlacement() {
        if (this.isTimeToAttack() && !this.mob.getInBlockState().is(GigTags.NEST_BLOCKS)) {
            if (this.delayBeforeAttack > 0) {
                this.delayBeforeAttack--;
                if (this.delayBeforeAttack == delayTicksBeforeAttack && !this.triggeredAttackAnimation) {
                    this.mob.animationDispatcher.sendLeftClaw();
                    this.triggeredAttackAnimation = true;
                }
            } else {
                this.resetAttackCooldown();
                NestBuildingHelper.tryBuildNestAround(this.mob.level(), this.mob.blockPosition(), this.mob);
                this.triggeredAttackAnimation = false;
            }
        } else {
            this.delayBeforeAttack = this.adjustedTickDelay(10);
            this.triggeredAttackAnimation = false;
        }
    }

    private int countNearbyAliens() {
        return this.mob.level()
            .getEntitiesOfClass(
                AlienEntity.class,
                this.mob.getBoundingBox().inflate(16),
                alien -> alien != this.mob && alien.isAlive()
            )
            .size();
    }
}
