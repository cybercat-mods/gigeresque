package mods.cybercat.gigeresque.common.entity.ai.brain;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import mods.cybercat.gigeresque.common.entity.ai.brain.task.FacehuggerPounceTask;
import mods.cybercat.gigeresque.common.entity.impl.FacehuggerEntity;
import mods.cybercat.gigeresque.common.util.EntityUtils;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.ForgetAttackTargetTask;
import net.minecraft.entity.ai.brain.task.LookAroundTask;
import net.minecraft.entity.ai.brain.task.RandomTask;
import net.minecraft.entity.ai.brain.task.RangedApproachTask;
import net.minecraft.entity.ai.brain.task.StayAboveWaterTask;
import net.minecraft.entity.ai.brain.task.StrollTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.UpdateAttackTargetTask;
import net.minecraft.entity.ai.brain.task.WaitTask;
import net.minecraft.entity.ai.brain.task.WalkTask;
import net.minecraft.entity.ai.brain.task.WanderAroundTask;

public class FacehuggerBrain extends ComplexBrain<FacehuggerEntity> {
	public FacehuggerBrain(FacehuggerEntity entity) {
		super(entity);
	}

	@Override
	protected void addCoreActivities(List<Task<? super FacehuggerEntity>> tasks) {
		tasks.add(new StayAboveWaterTask(0.8f));
		tasks.add(new WalkTask(0.4f));
		tasks.add(new LookAroundTask(45, 90));
		tasks.add(new WanderAroundTask());
	}

	@Override
	protected void addIdleActivities(List<Task<? super FacehuggerEntity>> tasks) {
		tasks.add(new UpdateAttackTargetTask<>((it) -> true, this::getPreferredTarget));

		tasks.add(avoidRepellentTask());
		tasks.add(makeRandomWanderTask());
	}

	@Override
	protected void addAvoidActivities(List<Task<? super FacehuggerEntity>> tasks) {

	}

	@Override
	protected void addFightActivities(List<Task<? super FacehuggerEntity>> tasks) {
		tasks.add(new ForgetAttackTargetTask<>(
				it -> brain.hasMemoryModule(MemoryModuleType.NEAREST_REPELLENT) || EntityUtils.isPotentialHost(it)
						|| ((Host) it).hasParasite() || EntityUtils.isFacehuggerAttached(it)));
		tasks.add(new RangedApproachTask(1.0f));
		tasks.add(new FacehuggerPounceTask());
	}

	private RandomTask<FacehuggerEntity> makeRandomWanderTask() {
		return new RandomTask<>(ImmutableList.of(Pair.of(new StrollTask(0.4f), 2), Pair.of(new WaitTask(30, 60), 1)));
	}
}
