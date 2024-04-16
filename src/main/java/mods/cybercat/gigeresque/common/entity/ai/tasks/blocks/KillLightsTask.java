package mods.cybercat.gigeresque.common.entity.ai.tasks.blocks;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.GigMemoryTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.GameRules;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KillLightsTask<E extends AlienEntity> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(GigMemoryTypes.NEARBY_LIGHT_BLOCKS.get(), MemoryStatus.VALUE_PRESENT));

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
        if (lightSourceLocation == null) return false;
        if (lightSourceLocation.stream().findFirst().isEmpty()) return false;
        var yDiff = Mth.abs(entity.getBlockY() - lightSourceLocation.stream().findFirst().get().getFirst().getY());
        var canGrief = entity.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        return !entity.isVehicle() && yDiff < 3 && !entity.isAggressive() && canGrief;
    }

    @Override
    protected void tick(@NotNull ServerLevel level, E entity, long gameTime) {
        var lightSourceLocation = entity.getBrain().getMemory(GigMemoryTypes.NEARBY_LIGHT_BLOCKS.get()).orElse(null);
        if (lightSourceLocation != null && lightSourceLocation.stream().findFirst().isPresent()) {
            var blockPos = lightSourceLocation.stream().findFirst().get().getFirst();
            if (!blockPos.closerToCenterThan(entity.position(), 3.4)) startMovingToTarget(entity, blockPos);
            if (blockPos.closerToCenterThan(entity.position(), 3.3)) {
                entity.triggerAnim(Constants.ATTACK_CONTROLLER, "swipe");
                entity.level().destroyBlock(blockPos, true, null, 512);
                if (!entity.level().isClientSide()) {
                    for (var i = 0; i < 2; i++)
                        ((ServerLevel) entity.level()).sendParticles(ParticleTypes.POOF,
                                ((double) blockPos.getX()) + 0.5, blockPos.getY(), ((double) blockPos.getZ()) + 0.5, 1,
                                entity.getRandom().nextGaussian() * 0.02, entity.getRandom().nextGaussian() * 0.02,
                                entity.getRandom().nextGaussian() * 0.02, 0.15000000596046448);
                }
            }
        }
    }

    private void startMovingToTarget(E alien, BlockPos targetPos) {
        alien.getNavigation().moveTo(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, 2.5F);
    }

}
