package com.bvanseg.gigeresque.common.entity.ai.brain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.GoToRememberedPositionTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public abstract class ComplexBrain<T extends LivingEntity> {
	protected T entity;
	Brain<T> brain;
	protected int timeStunned = 0;

	ComplexBrain(T entity) {
		this.entity = entity;
	}

	@SuppressWarnings("unchecked")
	public Brain<? extends T> initialize(Brain<? extends T> brain) {
		List<Task<? super T>> coreTasks = new ArrayList<>();
		addCoreActivities(coreTasks);

		List<Task<? super T>> idleTasks = new ArrayList<>();
		addIdleActivities(idleTasks);

		List<Task<? super T>> fightTasks = new ArrayList<>();
		addFightActivities(fightTasks);

		List<Task<? super T>> avoidTasks = new ArrayList<>();
		addAvoidActivities(avoidTasks);

		brain.setTaskList(Activity.CORE, 0, ImmutableList.<Task<? super T>>builder().addAll(coreTasks).build());
		brain.setTaskList(Activity.IDLE, 10, ImmutableList.<Task<? super T>>builder().addAll(idleTasks).build());
		brain.setTaskList(Activity.FIGHT, 10, ImmutableList.<Task<? super T>>builder().addAll(fightTasks).build(),
				MemoryModuleType.ATTACK_TARGET);
		brain.setTaskList(Activity.AVOID, 10, ImmutableList.<Task<? super T>>builder().addAll(avoidTasks).build(),
				MemoryModuleType.AVOID_TARGET);

		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.resetPossibleActivities();

		this.brain = (Brain<T>) brain;
		return brain;
	}

	public void tick() {
		if (timeStunned > 0) {
			timeStunned--;
			return;
		}

		brain.tick((ServerWorld) entity.world, entity);
	}

	public void tickActivities() {
		brain.resetPossibleActivities(ImmutableList.of(Activity.FIGHT, Activity.AVOID, Activity.IDLE));

		if (entity instanceof MobEntity) {
			((MobEntity) entity).setAttacking(brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET));
		}
	}

	protected abstract void addCoreActivities(List<Task<? super T>> tasks);

	protected void addIdleActivities(List<Task<? super T>> tasks) {
	}

	protected void addAvoidActivities(List<Task<? super T>> tasks) {
	}

	protected void addFightActivities(List<Task<? super T>> tasks) {
	}

	/*
	 * UTIL FUNCTIONS
	 */

	protected Optional<LivingEntity> getPreferredTarget(T entity) {
		Optional<LivingEntity> entityOptional = brain.getOptionalMemory(MemoryModuleType.NEAREST_ATTACKABLE);

		if (entityOptional.isEmpty()) {
			entityOptional = brain.getOptionalMemory(MemoryModuleType.ATTACK_TARGET);
		}
		return entityOptional;
	}

	/*
	 * DEFAULT TASK HELPERS
	 */

	protected GoToRememberedPositionTask<BlockPos> avoidRepellentTask() {
		return GoToRememberedPositionTask.toBlock(MemoryModuleType.NEAREST_REPELLENT, 1.0f, 8, false);
	}

	public void stun(int duration) {
		timeStunned = duration;
	}

}
