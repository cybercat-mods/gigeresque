package mods.cybercat.gigeresque.common.entity.ai.tasks.blocks;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.sblforked.api.core.behaviour.ExtendedBehaviour;
import mod.azure.azurelib.sblforked.util.BrainUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.GameRules;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import mods.cybercat.gigeresque.common.entity.ai.GigMemoryTypes;
import mods.cybercat.gigeresque.interfacing.AbstractAlien;

public class KillLightsTask<E extends PathfinderMob & AbstractAlien> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
        Pair.of(GigMemoryTypes.NEARBY_LIGHT_BLOCKS.get(), MemoryStatus.VALUE_PRESENT)
    );

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(E entity) {
        entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, E entity, long gameTime) {
        return !entity.isAggressive();
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, E entity) {
        var lightSourceLocation = entity.getBrain().getMemory(GigMemoryTypes.NEARBY_LIGHT_BLOCKS.get()).orElse(null);
        if (lightSourceLocation == null)
            return false;
        if (lightSourceLocation.stream().findFirst().isEmpty())
            return false;
        var yDiff = Mth.abs(entity.getBlockY() - lightSourceLocation.stream().findFirst().get().getFirst().getY());
        var canGrief = entity.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        return !entity.isVehicle() && yDiff < 3 && !entity.isAggressive() && canGrief;
    }

    @Override
    protected void tick(@NotNull ServerLevel level, E entity, long gameTime) {
        var lightSourceLocation = entity.getBrain().getMemory(GigMemoryTypes.NEARBY_LIGHT_BLOCKS.get()).orElse(null);
        if (lightSourceLocation != null && lightSourceLocation.stream().findFirst().isPresent()) {
            var blockPos = lightSourceLocation.stream().findFirst().get().getFirst();

            if (this.isBlockInViewAndReachable(entity, blockPos)) {
                entity.swing(InteractionHand.MAIN_HAND);
                entity.level().destroyBlock(blockPos, true, null, 512);
            } else {
                this.startMovingToTarget(entity, blockPos);
            }
        }
    }

    private boolean isBlockInViewAndReachable(E entity, BlockPos blockPos) {
        // Get the block position of the entity's current position
        var entityBlockPos = entity.blockPosition();

        // Check if the entity is exactly at the target block position
        if (entityBlockPos.equals(blockPos)) {
            return true;
        }

        if (entityBlockPos.above().equals(blockPos)) {
            return true;
        }

        // Otherwise, return false since the entity is not at the position
        return false;
    }

    private void startMovingToTarget(E alien, BlockPos targetPos) {
        BrainUtils.setMemory(alien, MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, 1.0F, 0));
    }

}
