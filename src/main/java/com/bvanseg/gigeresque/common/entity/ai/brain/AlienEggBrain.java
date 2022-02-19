package com.bvanseg.gigeresque.common.entity.ai.brain;

import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypes;
import com.bvanseg.gigeresque.common.entity.impl.AlienEggEntity;
import com.bvanseg.gigeresque.common.entity.impl.FacehuggerEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.UpdateAttackTargetTask;

import java.util.Collections;
import java.util.List;

public class AlienEggBrain extends ComplexBrain<AlienEggEntity> {
    public AlienEggBrain(AlienEggEntity entity) {
        super(entity);
    }

    @Override
    protected void addCoreActivities(List<Task<? super AlienEggEntity>> tasks) {
    }

    @Override
    protected void addIdleActivities(List<Task<? super AlienEggEntity>> tasks) {
        tasks.add(new UpdateAttackTargetTask<>((it) -> true, this::getPreferredTarget));
    }

    @Override
    protected void addAvoidActivities(List<Task<? super AlienEggEntity>> tasks) {
    }

    @Override
    protected void addFightActivities(List<Task<? super AlienEggEntity>> tasks) {
    }

    @Override
    public void tickActivities() {
        brain.resetPossibleActivities(
                ImmutableList.of(
                        Activity.FIGHT,
                        Activity.AVOID,
                        Activity.IDLE
                )
        );
    }

    private void tryHatch() {
        List<AlienEggEntity> nearestEggs = brain.getOptionalMemory(MemoryModuleTypes.NEAREST_EGGS).orElse(Collections.emptyList());
        List<FacehuggerEntity> nearestFacehuggers = brain.getOptionalMemory(MemoryModuleTypes.NEAREST_FACEHUGGERS).orElse(Collections.emptyList()).stream().filter(it -> it.ticksAttachedToHost < 0).toList();
        List<LivingEntity> nearestHosts = brain.getOptionalMemory(MemoryModuleTypes.NEAREST_HOSTS).orElse(Collections.emptyList());
        List<AlienEggEntity> nearestHatchingEggs = nearestEggs.stream().filter(it -> it.isHatching() || (it.isHatched() && it.hasFacehugger())).toList();

        if (nearestHosts.size() > (nearestFacehuggers.size() + nearestHatchingEggs.size())) {
            entity.setIsHatching(true);
        }
    }
}
