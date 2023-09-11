package mods.cybercat.gigeresque.common.entity.ai.tasks;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.GameRules;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;

public class KillCropsTask<E extends AlienEntity> extends ExtendedBehaviour<E> {

	private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(SBLMemoryTypes.NEARBY_BLOCKS.get(), MemoryStatus.VALUE_PRESENT));

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
		return !entity.isVehicle() && !entity.isAggressive();
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
		var lightSourceLocation = entity.getBrain().getMemory(SBLMemoryTypes.NEARBY_BLOCKS.get()).orElse(null);
		var yDiff = Mth.abs(entity.getBlockY() - lightSourceLocation.stream().findFirst().get().getFirst().getY());
		var canGrief = entity.getLevel().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
		return yDiff < 4 && !entity.isAggressive() && canGrief;
	}

	@Override
	protected void tick(ServerLevel level, E entity, long gameTime) {
		var lightSourceLocation = entity.getBrain().getMemory(SBLMemoryTypes.NEARBY_BLOCKS.get()).orElse(null);
		if (lightSourceLocation == null)
			return;
		if (!entity.isAggressive()) {
			if (!lightSourceLocation.stream().findFirst().get().getFirst().closerToCenterThan(entity.position(), 3.4))
				startMovingToTarget(entity, lightSourceLocation.stream().findFirst().get().getFirst());
			if (lightSourceLocation.stream().findFirst().get().getFirst().closerToCenterThan(entity.position(), 7.0)) {
				entity.swing(InteractionHand.MAIN_HAND);
				entity.getLevel().destroyBlock(lightSourceLocation.stream().findFirst().get().getFirst(), true, null, 512);
			}
		}
	}

	private void startMovingToTarget(E alien, BlockPos targetPos) {
		alien.getNavigation().moveTo(((double) ((float) targetPos.getX())) + 0.5, targetPos.getY(), ((double) ((float) targetPos.getZ())) + 0.5, 2.5F);
	}

}
