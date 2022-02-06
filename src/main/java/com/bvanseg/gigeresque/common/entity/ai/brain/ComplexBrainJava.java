package com.bvanseg.gigeresque.common.entity.ai.brain;

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

import java.util.List;
import java.util.Optional;

public abstract class ComplexBrainJava<T extends LivingEntity> {
    protected T entity;
    Brain<T> brain;
    protected int timeStunned = 0;

    ComplexBrainJava(T entity) {
        this.entity = entity;
    }


    public Brain<? extends T> initialize(Brain<? extends T> brain) {
        List<Task<? super T>> coreTasks = List.of();
        addCoreActivities(coreTasks);

        List<Task<? super T>> idleTasks = List.of();
        addIdleActivities(idleTasks);

        List<Task<? super T>> fightTasks = List.of();
        addFightActivities(fightTasks);

        List<Task<? super T>> avoidTasks = List.of();
        addAvoidActivities(avoidTasks);

        brain.setTaskList(Activity.CORE, 0, ImmutableList.<Task<? super T>>builder().addAll(coreTasks).build());
        brain.setTaskList(Activity.IDLE, 10, ImmutableList.<Task<? super T>>builder().addAll(idleTasks).build());
        brain.setTaskList(
                Activity.FIGHT,
                10,
                ImmutableList.<Task<? super T>>builder().addAll(fightTasks).build(),
                MemoryModuleType.ATTACK_TARGET
        );
        brain.setTaskList(
                Activity.AVOID,
                10,
                ImmutableList.<Task<? super T>>builder().addAll(avoidTasks).build(),
                MemoryModuleType.AVOID_TARGET
        );

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
        brain.resetPossibleActivities(
                ImmutableList.of(
                        Activity.FIGHT,
                        Activity.AVOID,
                        Activity.IDLE
                )
        );

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
        UTIL FUNCTIONS
     */

    protected Optional<LivingEntity> getPreferredTarget(T entity) {
        Optional<LivingEntity> entityOptional = brain.getOptionalMemory(MemoryModuleType.NEAREST_ATTACKABLE);

        if (entityOptional.isEmpty()) {
            entityOptional = brain.getOptionalMemory(MemoryModuleType.ATTACK_TARGET);
        }
        return entityOptional;
    }

    /*
        DEFAULT TASK HELPERS
     */

    protected GoToRememberedPositionTask<BlockPos> avoidRepellentTask() {
        return GoToRememberedPositionTask.toBlock(MemoryModuleType.NEAREST_REPELLENT, 1.0f, 8, false);
    }

    public void stun(int duration) {
        timeStunned = duration;
    }

}
