package mods.cybercat.gigeresque.common.entity.ai.tasks.misc;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.ai.GigMemoryTypes;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EatFoodTask<E extends ChestbursterEntity> extends DelayedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(GigMemoryTypes.FOOD_ITEMS.get(), MemoryStatus.VALUE_PRESENT));

    @Nullable
    protected LivingEntity target = null;

    public EatFoodTask(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return entity.isAlive();
    }

    @Override
    protected void start(E entity) {
        entity.setEatingStatus(false);
    }

    @Override
    protected void stop(E entity) {
        entity.setEatingStatus(false);
    }

    @Override
    protected void doDelayedAction(E entity) {
        var foodItem = entity.getBrain().getMemory(GigMemoryTypes.FOOD_ITEMS.get()).orElse(null);
        if (foodItem != null && foodItem.stream().findFirst().isPresent() && !entity.isAggressive()) {
            var blockPos = foodItem.stream().findFirst().get().blockPosition();
            var item = foodItem.stream().findFirst().get();
            // Check if the block is within the entity's view direction and reachable via pathfinding
            if (isBlockInViewAndReachable(entity, blockPos)) {
                entity.getNavigation().stop();
                entity.setEatingStatus(true);
                entity.triggerAnim(Constants.ATTACK_CONTROLLER, Constants.EAT);
                item.getItem().finishUsingItem(entity.level(), entity);
                item.getItem().shrink(1);
                entity.grow(entity, 2400.0f);
            } else {
                this.startMovingToTarget(entity, blockPos);
            }
        }
    }

    private boolean isBlockInViewAndReachable(E entity, BlockPos blockPos) {
        var blockCenter = Vec3.atCenterOf(blockPos);
        var entityPos = entity.position();
        // Calculate the squared distance between the entity and the block
        var distanceSquared = blockCenter.distanceToSqr(entityPos);

        // Check if the distance is less than or equal to one block
        if (distanceSquared <= 1.0) {
            // Don't start moving towards the target if already within one block distance
            return false;
        }

        // Check if the block is reachable via pathfinding
        var path = entity.getNavigation().createPath(blockPos, 0);
        return path != null && !path.isDone();
    }

    private void startMovingToTarget(E alien, BlockPos targetPos) {
        BrainUtils.setMemory(alien, MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, 0.7F, 0));
    }
}