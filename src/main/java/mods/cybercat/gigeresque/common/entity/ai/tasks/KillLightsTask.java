package mods.cybercat.gigeresque.common.entity.ai.tasks;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
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

import java.util.List;

public class KillLightsTask<E extends AlienEntity> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(GigMemoryTypes.NEARBY_LIGHT_BLOCKS.get(), MemoryStatus.VALUE_PRESENT));

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
        return !entity.isAggressive();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        var lightSourceLocation = entity.getBrain().getMemory(GigMemoryTypes.NEARBY_LIGHT_BLOCKS.get()).orElse(null);
        var yDiff = Mth.abs(entity.getBlockY() - lightSourceLocation.stream().findFirst().get().getFirst().getY());
        var canGrief = entity.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        return !entity.isVehicle() && yDiff < 3 && !entity.isAggressive() && canGrief;
    }

    @Override
    protected void tick(ServerLevel level, E entity, long gameTime) {
        var lightSourceLocation = entity.getBrain().getMemory(GigMemoryTypes.NEARBY_LIGHT_BLOCKS.get()).orElse(null);
        if (lightSourceLocation == null)
            return;
        if (!lightSourceLocation.stream().findFirst().get().getFirst().closerToCenterThan(entity.position(), 3.4))
            startMovingToTarget(entity, lightSourceLocation.stream().findFirst().get().getFirst());
        if (lightSourceLocation.stream().findFirst().get().getFirst().closerToCenterThan(entity.position(), 3.3)) {
            entity.triggerAnim("attackController", "swipe");
            entity.level().destroyBlock(lightSourceLocation.stream().findFirst().get().getFirst(), true, null, 512);
            if (!entity.level().isClientSide())
                for (var i = 0; i < 2; i++)
                    ((ServerLevel) entity.level()).sendParticles(ParticleTypes.POOF, ((double) lightSourceLocation.stream().findFirst().get().getFirst().getX()) + 0.5, lightSourceLocation.stream().findFirst().get().getFirst().getY(), ((double) lightSourceLocation.stream().findFirst().get().getFirst().getZ()) + 0.5, 1, entity.getRandom().nextGaussian() * 0.02, entity.getRandom().nextGaussian() * 0.02, entity.getRandom().nextGaussian() * 0.02, 0.15000000596046448);
        }
    }

    private void startMovingToTarget(E alien, BlockPos targetPos) {
        alien.getNavigation().moveTo(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, 2.5F);
    }

}
