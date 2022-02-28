package mods.cybercat.gigeresque.common.entity.ai.brain;

import java.util.List;

import mods.cybercat.gigeresque.common.entity.ai.brain.task.ConsumeFoodItemTask;
import mods.cybercat.gigeresque.common.entity.impl.AquaticChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.ChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.RunnerbursterEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.ForgetAttackTargetTask;
import net.minecraft.entity.ai.brain.task.ForgetTask;
import net.minecraft.entity.ai.brain.task.GoToRememberedPositionTask;
import net.minecraft.entity.ai.brain.task.LookAroundTask;
import net.minecraft.entity.ai.brain.task.MeleeAttackTask;
import net.minecraft.entity.ai.brain.task.RandomTask;
import net.minecraft.entity.ai.brain.task.RangedApproachTask;
import net.minecraft.entity.ai.brain.task.StayAboveWaterTask;
import net.minecraft.entity.ai.brain.task.StrollTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.UpdateAttackTargetTask;
import net.minecraft.entity.ai.brain.task.WaitTask;
import net.minecraft.entity.ai.brain.task.WalkTask;
import net.minecraft.entity.ai.brain.task.WanderAroundTask;

public class ChestbursterBrain extends ComplexBrain<ChestbursterEntity> {
	public ChestbursterBrain(ChestbursterEntity entity) {
		super(entity);
	}

	@Override
	protected void addCoreActivities(List<Task<? super ChestbursterEntity>> tasks) {
		if (!(entity instanceof AquaticChestbursterEntity)) {
			tasks.add(new StayAboveWaterTask(0.8f));
		}
		tasks.add(new WalkTask((entity instanceof RunnerbursterEntity) ? 1.0f : 2.0f));
		tasks.add(new LookAroundTask(45, 90));
		tasks.add(new WanderAroundTask());
	}

	@Override
	protected void addIdleActivities(List<Task<? super ChestbursterEntity>> tasks) {
		tasks.add(new UpdateAttackTargetTask<>((e) -> true, this::getPreferredTarget));

		tasks.add(new ConsumeFoodItemTask(1.0));
		tasks.add(avoidRepellentTask());
		tasks.add(makeRandomWanderTask());
	}

	@Override
	protected void addAvoidActivities(List<Task<? super ChestbursterEntity>> tasks) {
		tasks.add(GoToRememberedPositionTask.toEntity(MemoryModuleType.AVOID_TARGET,
				(entity instanceof RunnerbursterEntity) ? 1.5f : 1.0f, 12, true));
		tasks.add(makeRandomWanderTask());
		tasks.add(new ForgetTask<>((ChestbursterEntity chestburster) -> {
			if (brain.hasMemoryModule(MemoryModuleType.AVOID_TARGET)) {
				var avoidTarget = brain.getOptionalMemory(MemoryModuleType.AVOID_TARGET).orElse(null);
				if (avoidTarget == null)
					return false;
				if (avoidTarget.isDead()
						|| avoidTarget.getHeight()
								* avoidTarget.getWidth() < (chestburster.getHeight() * chestburster.getWidth()) * 3
						|| chestburster.distanceTo(avoidTarget) > 12) {
					return true;
				}
			}
			return false;
		}, MemoryModuleType.AVOID_TARGET));
	}

	@Override
	protected void addFightActivities(List<Task<? super ChestbursterEntity>> tasks) {
		tasks.add(new ForgetAttackTargetTask<>((it) -> {
			return brain.hasMemoryModule(MemoryModuleType.NEAREST_REPELLENT)
					|| brain.hasMemoryModule(MemoryModuleType.AVOID_TARGET);
		}));
		tasks.add(new RangedApproachTask((entity instanceof RunnerbursterEntity) ? 1.5f : 1.0f));
		tasks.add(new MeleeAttackTask(20));
	}

	private RandomTask<ChestbursterEntity> makeRandomWanderTask() {
		return new RandomTask<>(ImmutableList.of(com.mojang.datafixers.util.Pair.of(new StrollTask(1.0f), 2),
				Pair.of(new WaitTask(60, 120), 1)));
	}
}
