package mods.cybercat.gigeresque.common.entity.ai.goals.nest;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.LightLayer;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.nest.NestBuildingHelper;

public class BuildNestGoal extends Goal {

    protected final AlienEntity mob;

    public BuildNestGoal(AlienEntity mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        if (
            this.mob.level()
                .getBlockStatesIfLoaded(this.mob.getBoundingBox().inflate(10))
                .anyMatch(blockState -> blockState.is(GigTags.DUNGEON_BLOCKS) || blockState.is(GigTags.DUNGEON_STAIRS))
        ) {
            return false;
        }

        if (this.mob.isAggressive()) {
            return false;
        }

        if (!this.mob.crawlingManager.isCrawling()) {
            return false;
        }

        if (this.mob.isVehicle()) {
            return false;
        }

        if (this.mob.getGrowth() != this.mob.getMaxGrowth()) {
            return false;
        }

        if (!this.mob.level().canSeeSky(this.mob.blockPosition())) {
            return false;
        }

        if (this.mob.level().getBrightness(LightLayer.SKY, this.mob.blockPosition()) > 5) {
            return false;
        }

        if (this.mob.stasisManager.isStasis()) {
            return false;
        }

        if (this.mob.isFleeing()) {
            return false;
        }

        if (this.mob.level().dimensionType().piglinSafe()) {
            return false;
        }

        return this.mob.isAlive();
    }

    @Override
    public void start() {
        this.mob.animationDispatcher.sendLeftClaw();
    }

    @Override
    public void tick() {
        if (!this.mob.getInBlockState().is(GigTags.NEST_BLOCKS)) {
            NestBuildingHelper.tryBuildNestAround(this.mob.level(), this.mob.blockPosition(), this.mob);
        }
    }
}
