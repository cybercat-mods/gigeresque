package com.bvanseg.gigeresque.common.entity.ai.brain;

import com.bvanseg.gigeresque.common.entity.ai.brain.task.ConsumeFoodItemTask;
import com.bvanseg.gigeresque.common.entity.ai.brain.task.ConsumeFoodItemTaskJava;
import com.bvanseg.gigeresque.common.entity.impl.AquaticChestbursterEntityJava;
import com.bvanseg.gigeresque.common.entity.impl.ChestbursterEntityJava;
import com.bvanseg.gigeresque.common.entity.impl.RunnerbursterEntity;
import com.bvanseg.gigeresque.common.entity.impl.RunnerbursterEntityJava;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;

import java.util.List;

public class ChestbursterBrainJava extends ComplexBrainJava<ChestbursterEntityJava> {
    public ChestbursterBrainJava(ChestbursterEntityJava entity) {
        super(entity);
    }

    @Override
    protected void addCoreActivities(List<Task<? super ChestbursterEntityJava>> tasks) {
        if (!(entity instanceof AquaticChestbursterEntityJava)) {
            tasks.add(new StayAboveWaterTask(0.8f));
        }
        tasks.add(new WalkTask((entity instanceof RunnerbursterEntityJava) ? 1.0f : 2.0f));
        tasks.add(new LookAroundTask(45, 90));
        tasks.add(new WanderAroundTask());
    }

    @Override
    protected void addIdleActivities(List<Task<? super ChestbursterEntityJava>> tasks) {
        tasks.add(new UpdateAttackTargetTask<>((e) -> true, this::getPreferredTarget));

        tasks.add(new ConsumeFoodItemTaskJava(1.0));
        tasks.add(avoidRepellentTask());
        tasks.add(makeRandomWanderTask());
    }

    @Override
    protected void addAvoidActivities(List<Task<? super ChestbursterEntityJava>> tasks) {
        tasks.add(
                GoToRememberedPositionTask.toEntity(MemoryModuleType.AVOID_TARGET, (entity instanceof RunnerbursterEntityJava) ? 1.5f : 1.0f, 12, true)
        );
        tasks.add(makeRandomWanderTask());
        tasks.add(new ForgetTask<>((ChestbursterEntityJava chestburster) -> {
            if (brain.hasMemoryModule(MemoryModuleType.AVOID_TARGET)) {
                var avoidTarget = brain.getOptionalMemory(MemoryModuleType.AVOID_TARGET).orElse(null);
                if (avoidTarget == null) return false;
                if (avoidTarget.isDead() ||
                        avoidTarget.getHeight() * avoidTarget.getWidth() < (chestburster.getHeight() * chestburster.getWidth()) * 3 ||
                        chestburster.distanceTo(avoidTarget) > 12
                ) {
                    return true;
                }
            }
            return false;
        }, MemoryModuleType.AVOID_TARGET));
    }

    @Override
    protected void addFightActivities(List<Task<? super ChestbursterEntityJava>> tasks) {
        tasks.add(new ForgetAttackTargetTask<>((it) -> {
            return brain.hasMemoryModule(MemoryModuleType.NEAREST_REPELLENT) ||
                    brain.hasMemoryModule(MemoryModuleType.AVOID_TARGET);
        }));
        tasks.add(new RangedApproachTask((entity instanceof RunnerbursterEntityJava) ? 1.5f : 1.0f));
        tasks.add(new MeleeAttackTask(20));
    }

    private RandomTask<ChestbursterEntityJava> makeRandomWanderTask() {
        return new RandomTask<>(ImmutableList.of(com.mojang.datafixers.util.Pair.of(new StrollTask(1.0f), 2), Pair.of(new WaitTask(60, 120), 1)));
    }
}
