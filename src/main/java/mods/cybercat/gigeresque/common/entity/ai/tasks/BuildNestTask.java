package mods.cybercat.gigeresque.common.entity.ai.tasks;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.cybercat.gigeresque.common.entity.impl.AdultAlienEntity;
import mods.cybercat.gigeresque.common.util.nest.NestBuildingHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

public class BuildNestTask<E extends AdultAlienEntity> extends ExtendedBehaviour<E> {

	private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
			Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
			Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT));
	private double cooldown = 0;

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, E alien) {
		cooldown = Mth.absMax(cooldown - 1, 0);
		return alien.getGrowth() == alien.getMaxGrowth() && !alien.level.canSeeSky(alien.blockPosition());
	}

	@Override
	protected void tick(ServerLevel level, E alien, long gameTime) {
		NestBuildingHelper.tryBuildNestAround(alien);
		cooldown += 180;
	}
}
