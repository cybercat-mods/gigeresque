package mods.cybercat.gigeresque.common.entity.ai.tasks.blocks;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.GigMemoryTypes;

public class KillLightsTask<E extends AlienEntity> extends ExtendedBehaviour<E> {

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

            // Check if the block is within the entity's view direction and reachable via pathfinding
            if (isBlockInViewAndReachable(entity, blockPos)) {
                entity.triggerAnim(Constants.ATTACK_CONTROLLER, "swipe");
                entity.level().destroyBlock(blockPos, true, null, 512);
                if (!entity.level().isClientSide()) {
                    for (var i = 0; i < 2; i++) {
                        level.sendParticles(
                            ParticleTypes.POOF,
                            ((double) blockPos.getX()) + 0.5,
                            blockPos.getY(),
                            ((double) blockPos.getZ()) + 0.5,
                            1,
                            entity.getRandom().nextGaussian() * 0.02,
                            entity.getRandom().nextGaussian() * 0.02,
                            entity.getRandom().nextGaussian() * 0.02,
                            0.15000000596046448
                        );
                    }
                }
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
