package mods.cybercat.gigeresque.common.entity.ai.goals.nest;

import mod.azure.azurelib.sblforked.util.RandomUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class EggmorphGoal extends Goal {

    protected final AlienEntity mob;

    private Optional<BlockPos> targetBlock = Optional.empty();

    public EggmorphGoal(AlienEntity mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        this.targetBlock = findNearestNestBlock();
        return this.targetBlock.isPresent() && this.mob.isVehicle();
    }

    @Override
    public boolean canContinueToUse() {
        return this.targetBlock.isPresent()
            && !this.mob.isFleeing()
            && this.mob.isVehicle()
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
        if (
            this.targetBlock.isEmpty() || !this.mob.level().getBlockState(this.targetBlock.get()).is(GigBlocks.NEST_RESIN_WEB_CROSS.get())
        ) {
            this.targetBlock = findNearestNestBlock();
        }
        this.targetBlock.ifPresent(blockPos -> {
            if (isPathfindable(blockPos)) {
                var centerPos = Vec3.atCenterOf(blockPos);
                var blockCenter = Vec3.atCenterOf(blockPos);
                var entityPos = this.mob.position();
                var passenger = this.mob.getFirstPassenger();
                var test = RandomUtil.getRandomPositionWithinRange(this.mob.blockPosition(), 3, 1, 3, false, this.mob.level());

                this.mob.getNavigation().moveTo(centerPos.x, centerPos.y, centerPos.z, 0.8F);

                if (
                    blockCenter.distanceToSqr(entityPos) <= 9.0 && passenger != null && this.mob.level() instanceof ServerLevel serverLevel
                ) {
                    this.placeInNest(test, serverLevel, this.mob, passenger);
                }
            } else {
                this.targetBlock = Optional.empty();
            }
        });
    }

    private Optional<BlockPos> findNearestNestBlock() {
        var mobPos = this.mob.blockPosition();
        var searchRadius = 15;

        return BlockPos.betweenClosedStream(
            mobPos.offset(-searchRadius, -1, -searchRadius),
            mobPos.offset(searchRadius, 1, searchRadius)
        )
            .filter(pos -> this.mob.level().getBlockState(pos).is(GigBlocks.NEST_RESIN_WEB_CROSS.get()))
            .filter(this::isPathfindable)
            .findFirst();
    }

    private boolean isPathfindable(BlockPos blockPos) {
        return this.mob.getNavigation().createPath(blockPos, 0) != null;
    }

    private void placeInNest(BlockPos test, @NotNull ServerLevel level, AlienEntity entity, Entity passenger) {
        for (BlockPos testPos : BlockPos.betweenClosed(test, test.above(2))) {
            if (
                level.getBlockState(test).isAir() && level.getBlockState(
                    test.below()
                ).isSolid() && level.getEntitiesOfClass(
                    LivingEntity.class,
                    new AABB(test)
                ).stream().noneMatch(Objects::isNull) && passenger != null
            ) {
                passenger.setPos(Vec3.atBottomCenterOf(testPos));
                passenger.removeVehicle();
                passenger.ejectPassengers();
                entity.animationDispatcher.sendLeftClaw();
                level.setBlockAndUpdate(testPos, GigBlocks.NEST_RESIN_WEB_CROSS.get().defaultBlockState());
                level.setBlockAndUpdate(testPos.above(), GigBlocks.NEST_RESIN_WEB_CROSS.get().defaultBlockState());
                entity.ejectPassengers();
                entity.removeVehicle();
            }
        }
    }
}
