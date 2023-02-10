package mods.cybercat.gigeresque.common.entity.ai.tasks;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;

public class FleeFireTask<E extends PathfinderMob> extends ExtendedBehaviour<E> {

	private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList
			.of(Pair.of(SBLMemoryTypes.NEARBY_BLOCKS.get(), MemoryStatus.VALUE_PRESENT));
	protected final float speed;
	
	public FleeFireTask(float speed) {
		this.speed = speed;
	}
	
	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

    @Override
    protected void start(ServerLevel level, PathfinderMob entity, long gameTime) {
        entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, PathfinderMob entity, long gameTime) {
        return true;
    }

    @Override
    protected void tick(ServerLevel level, PathfinderMob owner, long gameTime) {
        Vec3 vec3;
        if (owner.getNavigation().isDone() && (vec3 = this.getPanicPos(owner, level)) != null) 
            owner.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(vec3, this.speed, 0));
    }

    @Nullable
    private Vec3 getPanicPos(PathfinderMob pathfinder, ServerLevel level) {
        Optional<Vec3> optional;
        if (pathfinder.isOnFire() && (optional = this.lookForWater(level, pathfinder).map(Vec3::atBottomCenterOf)).isPresent()) 
            return optional.get();
        return LandRandomPos.getPos(pathfinder, 15, 4);
    }

    private Optional<BlockPos> lookForWater(BlockGetter level, Entity entity) {
        BlockPos blockPos2 = entity.blockPosition();
        if (!level.getBlockState(blockPos2).getCollisionShape(level, blockPos2).isEmpty()) 
            return Optional.empty();
        return BlockPos.findClosestMatch(blockPos2, 5, 1, blockPos -> level.getFluidState((BlockPos)blockPos).is(FluidTags.WATER));
    }

}
