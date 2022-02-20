package com.bvanseg.gigeresque.common.entity.ai.brain.sensor;

import java.util.List;
import java.util.Set;

import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypes;
import com.bvanseg.gigeresque.common.entity.impl.FacehuggerEntity;
import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.LivingTargetCache;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.server.world.ServerWorld;

public class NearestFacehuggersSensor extends Sensor<LivingEntity> {
	@Override
	public Set<MemoryModuleType<?>> getOutputMemoryModules() {
		return ImmutableSet.of(MemoryModuleTypes.NEAREST_FACEHUGGERS);
	}

	@Override
	protected void sense(ServerWorld world, LivingEntity entity) {
		Brain<?> brain = entity.getBrain();
		LivingTargetCache nearestVisibleMobs = brain.getOptionalMemory(MemoryModuleType.VISIBLE_MOBS)
				.orElse(LivingTargetCache.empty());

		List<FacehuggerEntity> nearestFacehuggers = nearestVisibleMobs.stream(it -> it instanceof FacehuggerEntity)
				.map(it -> (FacehuggerEntity) it).toList();
		brain.remember(MemoryModuleTypes.NEAREST_FACEHUGGERS, nearestFacehuggers);
	}
}
