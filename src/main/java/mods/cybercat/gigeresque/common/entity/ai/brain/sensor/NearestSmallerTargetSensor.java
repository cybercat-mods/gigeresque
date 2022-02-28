package mods.cybercat.gigeresque.common.entity.ai.brain.sensor;

import java.util.Set;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.LivingTargetCache;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.server.world.ServerWorld;

public class NearestSmallerTargetSensor extends Sensor<LivingEntity> {
	@Override
	public Set<MemoryModuleType<?>> getOutputMemoryModules() {
		return ImmutableSet.of(MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.VISIBLE_MOBS);
	}

	@Override
	protected void sense(ServerWorld world, LivingEntity entity) {
		Brain<?> brain = entity.getBrain();

		LivingTargetCache nearestVisibleMobs = brain.getOptionalMemory(MemoryModuleType.VISIBLE_MOBS)
				.orElse(LivingTargetCache.empty());

		nearestVisibleMobs.findFirst(it -> {
			if (it instanceof AlienEntity)
				return false;
			return it.getHeight() * it.getWidth() < (entity.getHeight() * entity.getWidth()) * 3;
		}).ifPresent(it -> brain.remember(MemoryModuleType.NEAREST_ATTACKABLE, it));
	}
}
