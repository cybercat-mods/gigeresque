package mods.cybercat.gigeresque.common.entity.ai.tasks;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.GigMemoryTypes;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

public class EggmorpthTargetTask<E extends AlienEntity> extends ExtendedBehaviour<E> {

	private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList
			.of(Pair.of(GigMemoryTypes.NEARBY_NEST_BLOCKS.get(), MemoryStatus.VALUE_PRESENT));

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
		return !entity.isVehicle();
	}

	@Override
	protected void tick(ServerLevel level, E entity, long gameTime) {
		var lightSourceLocation = entity.getBrain().getMemory(GigMemoryTypes.NEARBY_NEST_BLOCKS.get()).orElse(null);
		var target = entity.getFirstPassenger();
		if (lightSourceLocation == null)
			return;
		if (target == null)
			return;
		var nearestWebbing = lightSourceLocation.stream().findFirst().get().getFirst();
		if (nearestWebbing == null)
			return;
		if (!lightSourceLocation.stream().findFirst().get().getFirst().closerToCenterThan(entity.position(), 1.4))
			startMovingToTarget(entity, lightSourceLocation.stream().findFirst().get().getFirst());
		if (lightSourceLocation.stream().findFirst().get().getFirst().closerToCenterThan(entity.position(), 1.4)) {
			((Eggmorphable) target).setTicksUntilEggmorphed(GigeresqueConfig.getEggmorphTickTimer());
			target.stopRiding();
			target.setPos(Vec3.atBottomCenterOf(lightSourceLocation.stream().findFirst().get().getFirst()));

			var hasCeiling = false;

			var ceilingUp = nearestWebbing.above();
			for (int i = 0; i < 20; i++) {
				if (entity.level.getBlockState(ceilingUp).isSolidRender(entity.level, ceilingUp)) {
					hasCeiling = true;
					break;
				}
				ceilingUp = ceilingUp.above();
			}

			var up = nearestWebbing.above();
			if (hasCeiling) {
				for (int i = 0; i < 20; i++) {
					var state = entity.level.getBlockState(up);

					if (state.isAir() || state.getBlock() == GIgBlocks.NEST_RESIN_WEB) {
						entity.level.setBlockAndUpdate(up, GIgBlocks.NEST_RESIN_WEB_CROSS.defaultBlockState());
					} else {
						break;
					}
					up = up.above();
				}
			}
		}
	}

	private void startMovingToTarget(E alien, BlockPos targetPos) {
		alien.getNavigation().moveTo(((double) ((float) targetPos.getX())) + 0.5, targetPos.getY(),
				((double) ((float) targetPos.getZ())) + 0.5, 2.5F);
	}

}
