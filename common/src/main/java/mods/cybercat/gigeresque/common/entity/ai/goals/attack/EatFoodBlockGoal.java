package mods.cybercat.gigeresque.common.entity.ai.goals.attack;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;

public class EatFoodBlockGoal extends Goal {

    protected final AlienEntity mob;

    private Optional<BlockPos> targetBlock = Optional.empty();

    private int breakTimer;

    public EatFoodBlockGoal(AlienEntity mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        this.targetBlock = findNearestDestructibleLight();
        return this.targetBlock.isPresent() && !this.mob.isAggressive() && !this.mob.isVehicle();
    }

    @Override
    public boolean canContinueToUse() {
        return this.targetBlock.isPresent()
            && !this.mob.isAggressive()
            && !this.mob.isFleeing()
            && !this.mob.isVehicle()
            && !this.mob.stasisManager.isStasis();
    }

    @Override
    public void start() {
        this.mob.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.targetBlock = Optional.empty();
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.targetBlock.isEmpty() || !this.mob.level().getBlockState(this.targetBlock.get()).is(GigTags.BURSTER_BLOCKS)) {
            this.targetBlock = findNearestDestructibleLight();
        }
        this.targetBlock.ifPresent(blockPos -> {
            if (isPathfindable(blockPos)) {
                Vec3 centerPos = Vec3.atCenterOf(blockPos);

                this.mob.getNavigation().moveTo(centerPos.x, centerPos.y, centerPos.z, 1.0);

                if (isBlockInViewAndReachable(blockPos)) {
                    if (!this.mob.level().isClientSide) {
                        this.breakTimer++;
                    }
                    if (this.breakTimer > 5) {
                        this.mob.animationDispatcher.sendChomp();
                    }
                    if (this.breakTimer >= 10) {
                        this.mob.swing(InteractionHand.MAIN_HAND);
                        if (!this.mob.level().isClientSide) {
                            this.mob.level().destroyBlock(blockPos, true, this.mob);
                        }
                        this.targetBlock = Optional.empty();
                        this.breakTimer = 0;
                    }
                }
            } else {
                this.targetBlock = Optional.empty();
            }
        });
    }

    private Optional<BlockPos> findNearestDestructibleLight() {
        var mobPos = this.mob.blockPosition();
        var searchRadius = 15;

        return BlockPos.betweenClosedStream(
            mobPos.offset(-searchRadius, -1, -searchRadius),
            mobPos.offset(searchRadius, 1, searchRadius)
        )
            .filter(pos -> this.mob.level().getBlockState(pos).is(GigTags.BURSTER_BLOCKS))
            .filter(this::isPathfindable)
            .findFirst();
    }

    private boolean isPathfindable(BlockPos blockPos) {
        return this.mob.getNavigation().createPath(blockPos, 0) != null;
    }

    private boolean isBlockInViewAndReachable(BlockPos blockPos) {
        double reachDistance = 2.0;
        return this.mob.distanceToSqr(Vec3.atCenterOf(blockPos)) <= reachDistance * reachDistance;
    }

}
