package mods.cybercat.gigeresque.common.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public class AlienPanic extends Behavior<PathfinderMob> {
    private static final Predicate<PathfinderMob> DEFAULT_SHOULD_PANIC_PREDICATE = pathfinderMob -> pathfinderMob.getLastHurtByMob() != null || pathfinderMob.isFreezing() || pathfinderMob.isOnFire();
    private final float speedMultiplier;
    private final Predicate<PathfinderMob> shouldPanic;

    public AlienPanic(float f) {
        this(f, DEFAULT_SHOULD_PANIC_PREDICATE);
    }

    public AlienPanic(float f, Predicate<PathfinderMob> predicate) {
        super(ImmutableMap.of(MemoryModuleType.IS_PANICKING, MemoryStatus.REGISTERED, MemoryModuleType.HURT_BY, MemoryStatus.VALUE_PRESENT), 20, 40);
        this.speedMultiplier = f;
        this.shouldPanic = predicate;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel serverLevel, @NotNull PathfinderMob pathfinderMob) {
        return this.shouldPanic.test(pathfinderMob) && !pathfinderMob.isAggressive();
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel serverLevel, @NotNull PathfinderMob pathfinderMob, long l) {
        return true;
    }

    @Override
    protected void start(@NotNull ServerLevel serverLevel, PathfinderMob pathfinderMob, long l) {
        pathfinderMob.getBrain().setMemory(MemoryModuleType.IS_PANICKING, true);
        pathfinderMob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    @Override
    protected void stop(@NotNull ServerLevel serverLevel, PathfinderMob pathfinderMob, long l) {
        Brain<?> brain = pathfinderMob.getBrain();
        brain.eraseMemory(MemoryModuleType.IS_PANICKING);
    }

    @Override
    protected void tick(@NotNull ServerLevel serverLevel, PathfinderMob pathfinderMob, long l) {
        Vec3 vec3;
        if (pathfinderMob.getNavigation().isDone() && (vec3 = this.getPanicPos(pathfinderMob, serverLevel)) != null) {
            pathfinderMob.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(vec3, this.speedMultiplier, 0));
        }
    }

    @Nullable
    private Vec3 getPanicPos(PathfinderMob pathfinderMob, ServerLevel serverLevel) {
        Optional<Vec3> optional;
        if (pathfinderMob.isOnFire() && (optional = this.lookForWater(serverLevel, pathfinderMob).map(Vec3::atBottomCenterOf)).isPresent()) {
            return optional.get();
        }
        return LandRandomPos.getPos(pathfinderMob, 5, 4);
    }

    private Optional<BlockPos> lookForWater(BlockGetter blockGetter, Entity entity) {
        BlockPos blockPos2 = entity.blockPosition();
        if (!blockGetter.getBlockState(blockPos2).getCollisionShape(blockGetter, blockPos2).isEmpty()) {
            return Optional.empty();
        }
        return BlockPos.findClosestMatch(blockPos2, 5, 1, blockPos -> blockGetter.getFluidState(blockPos).is(FluidTags.WATER));
    }
}