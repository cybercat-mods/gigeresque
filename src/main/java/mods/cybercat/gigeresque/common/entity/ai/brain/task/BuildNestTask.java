package mods.cybercat.gigeresque.common.entity.ai.brain.task;

import static java.lang.Math.max;

import mods.cybercat.gigeresque.common.entity.impl.AdultAlienEntity;
import mods.cybercat.gigeresque.common.util.nest.NestBuildingHelper;
import com.google.common.collect.ImmutableMap;

import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;

public class BuildNestTask extends Task<AdultAlienEntity> {
	private int cooldown = 0;

	public BuildNestTask() {
		super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
				MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.HOME,
				MemoryModuleState.VALUE_PRESENT));
	}

	@Override
	public boolean shouldRun(ServerWorld serverWorld, AdultAlienEntity alien) {
		var homePos = alien.getBrain().getOptionalMemory(MemoryModuleType.HOME).orElse(null);
		if (homePos == null)
			return false;

		var isWithinNestRange = homePos.getPos().getManhattanDistance(alien.getBlockPos()) < 50;
		cooldown = max(cooldown - 1, 0);
		return alien.getGrowth() == alien.getMaxGrowth() && cooldown <= 0 && isWithinNestRange
				&& !alien.world.isSkyVisible(alien.getBlockPos()) && alien.world.getAmbientDarkness() <= 9;
	}

	@Override
	public void run(ServerWorld serverWorld, AdultAlienEntity alien, long l) {
		NestBuildingHelper.tryBuildNestAround(alien);
		cooldown += 180;
	}
}
