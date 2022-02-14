package com.bvanseg.gigeresque.common.entity.ai.brain;

import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypesJava;
import com.bvanseg.gigeresque.common.entity.impl.AlienEggEntityJava;
import com.bvanseg.gigeresque.common.entity.impl.FacehuggerEntityJava;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.UpdateAttackTargetTask;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AlienEggBrainJava extends ComplexBrainJava<AlienEggEntityJava> {
    public AlienEggBrainJava(AlienEggEntityJava entity) {
        super(entity);
    }

    @Override
    protected void addCoreActivities(List<Task<? super AlienEggEntityJava>> tasks) {
    }

    @Override
    protected void addIdleActivities(List<Task<? super AlienEggEntityJava>> tasks) {
        tasks.add(new UpdateAttackTargetTask<>((it) -> true, this::getPreferredTarget));
    }

    @Override
    protected void addAvoidActivities(List<Task<? super AlienEggEntityJava>> tasks) {
    }

    @Override
    protected void addFightActivities(List<Task<? super AlienEggEntityJava>> tasks) {
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
        List<AlienEggEntityJava> nearestEggs = brain.getOptionalMemory(MemoryModuleTypesJava.NEAREST_EGGS).orElse(Collections.emptyList());
        List<FacehuggerEntityJava> nearestFacehuggers = brain.getOptionalMemory(MemoryModuleTypesJava.NEAREST_FACEHUGGERS).orElse(Collections.emptyList()).stream().filter(it -> it.ticksAttachedToHost < 0).toList();
        List<LivingEntity> nearestHosts = brain.getOptionalMemory(MemoryModuleTypesJava.NEAREST_HOSTS).orElse(Collections.emptyList());
        List<AlienEggEntityJava> nearestHatchingEggs = nearestEggs.stream().filter(it -> it.isHatching() || (it.isHatched() && it.hasFacehugger())).toList();

        if (nearestHosts.size() > (nearestFacehuggers.size() + nearestHatchingEggs.size())) {
            entity.setIsHatching(true);
        }
    }
}
