package mods.cybercat.gigeresque.common.entity.ai.tasks.movement;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.interfacing.AbstractAlien;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class FindDarknessTask<E extends PathfinderMob & AbstractAlien & GeoEntity> extends ExtendedBehaviour<E> {
    public static final Predicate<BlockState> NEST = state -> state.is(GigBlocks.NEST_RESIN_WEB_CROSS.get());
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT),
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED));
    protected float speedModifier = 1;
    protected Vec3 hidePos = null;

    public FindDarknessTask<E> speedModifier(float speedMod) {
        this.speedModifier = speedMod;

        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull E entity) {

        this.hidePos = getHidePos(entity);

        return entity.level().getBlockStatesIfLoaded(entity.getBoundingBox().inflate(32)).noneMatch(
                NEST) && !entity.isVehicle() && !entity.isAggressive() && !entity.isPassedOut() && this.hidePos != null && entity.level().getBrightness(
                LightLayer.SKY, entity.blockPosition()) > 5;
    }

    @Override
    protected void start(E entity) {
        BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(this.hidePos, 2.5F, 0));
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        if (this.hidePos == null)
            return false;

        var walkTarget = BrainUtils.getMemory(entity, MemoryModuleType.WALK_TARGET);

        if (walkTarget == null)
            return false;

        return walkTarget.getTarget().currentBlockPosition().equals(
                BlockPos.containing(this.hidePos)) && !entity.getNavigation().isDone();
    }

    @Override
    protected void stop(E entity) {
        this.hidePos = null;
    }

    @Nullable
    protected Vec3 getHidePos(E entity) {
        var randomsource = entity.getRandom();
        var entityPos = entity.blockPosition();

        for (var i = 0; i < 50; ++i) {
            var runPos = entityPos.offset(randomsource.nextInt(20) - 50, randomsource.nextInt(6) - 3,
                    randomsource.nextInt(20) - 50);

            if (!entity.level().canSeeSky(runPos) && entity.getWalkTargetValue(runPos) < 0.0F)
                return Vec3.atBottomCenterOf(runPos);
        }

        return null;
    }
}
