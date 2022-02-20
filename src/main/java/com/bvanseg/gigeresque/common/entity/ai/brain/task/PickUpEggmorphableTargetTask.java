package com.bvanseg.gigeresque.common.entity.ai.brain.task;

import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypes;
import com.bvanseg.gigeresque.common.entity.impl.AdultAlienEntity;
import com.bvanseg.gigeresque.common.util.EntityUtils;
import com.google.common.collect.ImmutableMap;

import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;

public class PickUpEggmorphableTargetTask extends Task<AdultAlienEntity> {
	private final double speed;

	public PickUpEggmorphableTargetTask(double speed) {
		super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
				MemoryModuleTypes.EGGMORPH_TARGET, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.HOME,
				MemoryModuleState.VALUE_PRESENT));
		this.speed = speed;
	}

	@Override
	protected boolean shouldRun(ServerWorld world, AdultAlienEntity alien) {
		if (alien.getBrain().hasMemoryModule(MemoryModuleTypes.EGGMORPH_TARGET)) {
			var target = alien.getBrain().getOptionalMemory(MemoryModuleTypes.EGGMORPH_TARGET).orElse(null);
			if (target == null)
				return false;
			if (EntityUtils.isEggmorphable(target) && !EntityUtils.isFacehuggerAttached(target)) {
				return !alien.isCarryingEggmorphableTarget();
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	protected void run(ServerWorld serverWorld, AdultAlienEntity alien, long l) {
		var target = alien.getBrain().getOptionalMemory(MemoryModuleTypes.EGGMORPH_TARGET).orElse(null);
		if (target == null)
			return;

		if (alien.distanceTo(target) < 4.0) {
			if (target.getVehicle() == null) {
				target.startRiding(alien);
			}
		} else {
			alien.getNavigation().startMovingTo(target.getX(), target.getY(), target.getZ(), speed);
		}
	}
}
