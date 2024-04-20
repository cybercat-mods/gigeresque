package mods.cybercat.gigeresque.common.entity.ai.tasks.blocks;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mods.cybercat.gigeresque.interfacing.AbstractAlien;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KillCropsTask<E extends PathfinderMob & AbstractAlien & GeoEntity> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(SBLMemoryTypes.NEARBY_BLOCKS.get(), MemoryStatus.VALUE_PRESENT));

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(E entity) {
        entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, E entity, long gameTime) {
        return !entity.isVehicle() && !entity.isAggressive();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        var lightSourceLocation = entity.getBrain().getMemory(SBLMemoryTypes.NEARBY_BLOCKS.get()).orElse(null);
        var yDiff = Mth.abs(entity.getBlockY() - lightSourceLocation.stream().findFirst().get().getFirst().getY());
        var canGrief = entity.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        return yDiff < 4 && !entity.isAggressive() && canGrief;
    }

    @Override
    protected void tick(@NotNull ServerLevel level, E entity, long gameTime) {
        var cropBlock = entity.getBrain().getMemory(SBLMemoryTypes.NEARBY_BLOCKS.get()).orElse(null);
        if (cropBlock != null && cropBlock.stream().findFirst().isPresent() && !entity.isAggressive()) {
            var blockPos = cropBlock.stream().findFirst().get().getFirst();

            // Check if the block is within the entity's view direction and reachable via pathfinding
            if (isBlockInViewAndReachable(entity, blockPos)) {
                entity.swing(InteractionHand.MAIN_HAND);
                entity.level().destroyBlock(blockPos, true, null, 512);
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
        BrainUtils.setMemory(alien, MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, 2.5F, 0));
    }

}
