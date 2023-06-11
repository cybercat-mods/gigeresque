package mods.cybercat.gigeresque.common.entity.ai.tasks;

import java.util.List;
import java.util.function.Predicate;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.GigMemoryTypes;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.tslat.smartbrainlib.util.RandomUtil;

public class EggmorpthTargetTask<E extends AlienEntity> extends ExtendedBehaviour<E> {

	private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(GigMemoryTypes.NEARBY_NEST_BLOCKS.get(), MemoryStatus.VALUE_PRESENT));
	public static final Predicate<BlockState> NEST = state -> state.is(GIgBlocks.NEST_RESIN_WEB_CROSS);

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
		return entity.getEntityData().get(AlienEntity.FLEEING_FIRE).booleanValue() == false;
	}

	@Override
	protected void tick(ServerLevel level, E entity, long gameTime) {
		var lightSourceLocation = entity.getBrain().getMemory(GigMemoryTypes.NEARBY_NEST_BLOCKS.get()).orElse(null);
		var target = entity.getFirstPassenger();
		if (lightSourceLocation == null)
			return;
		var test = RandomUtil.getRandomPositionWithinRange(entity.blockPosition(), 3, 1, 3, false, level);
		var nestLocation = lightSourceLocation.stream().findAny().get().getFirst();
//		if (!lightSourceLocation.stream().findAny().get().getSecond().is(GIgBlocks.NEST_RESIN_WEB_CROSS))
//		return;
		if (target != null)
			if (test != nestLocation)
				if (!nestLocation.closerToCenterThan(entity.position(), 1.4))
					BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(nestLocation, 2.5F, 0));
				else {
					for (BlockPos testPos : BlockPos.betweenClosed(test, test.above(2)))
						if (level.getBlockState(test).isAir() && level.getBlockState(test.below()).getMaterial().isSolid()) {
							var yDiff = Mth.abs(entity.getBlockY() - lightSourceLocation.stream().findFirst().get().getFirst().getY());
							if (yDiff < 2) {
								BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
								((Eggmorphable) target).setTicksUntilEggmorphed(Gigeresque.config.getEggmorphTickTimer());
								target.setPos(Vec3.atBottomCenterOf(testPos));
								target.removeVehicle();
								entity.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
								level.setBlockAndUpdate(testPos, GIgBlocks.NEST_RESIN_WEB_CROSS.defaultBlockState());
								level.setBlockAndUpdate(testPos.above(), GIgBlocks.NEST_RESIN_WEB_CROSS.defaultBlockState());
							}
						}
				}
	}

}
