package com.bvanseg.gigeresque.common.entity.ai.brain.sensor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.server.world.ServerWorld;

public class NearestFoodItemSensor extends Sensor<LivingEntity> {
	@Override
	public Set<MemoryModuleType<?>> getOutputMemoryModules() {
		return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
	}

	@Override
	protected void sense(ServerWorld world, LivingEntity entity) {
		Brain<?> brain = entity.getBrain();

		List<ItemEntity> foodItems = entity.world.getEntitiesByClass(ItemEntity.class,
				entity.getBoundingBox().expand(4.0), it -> it.getStack().isFood());
		brain.remember(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
				Optional.ofNullable(foodItems.isEmpty() ? null : foodItems.get(0)));
	}
}
