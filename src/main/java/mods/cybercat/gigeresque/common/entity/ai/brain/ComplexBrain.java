package mods.cybercat.gigeresque.common.entity.ai.brain;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;

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
	}

	protected abstract void addCoreActivities(List<Task<? super T>> tasks);

	protected void addIdleActivities(List<Task<? super T>> tasks) {
	}

	protected void addAvoidActivities(List<Task<? super T>> tasks) {
	}

	protected void addFightActivities(List<Task<? super T>> tasks) {
	}

}