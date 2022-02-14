package com.bvanseg.gigeresque.common.entity.ai.brain;

import com.bvanseg.gigeresque.common.entity.ai.brain.task.FacehuggerPounceTaskJava;
import com.bvanseg.gigeresque.common.entity.impl.FacehuggerEntityJava;
import com.bvanseg.gigeresque.common.util.EntityUtils;
import com.bvanseg.gigeresque.interfacing.Host;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;

import java.util.List;

public class FacehuggerBrainJava extends ComplexBrainJava<FacehuggerEntityJava> {
    public FacehuggerBrainJava(FacehuggerEntityJava entity) {
        super(entity);
    }

    @Override
    protected void addCoreActivities(List<Task<? super FacehuggerEntityJava>> tasks) {
        tasks.add(new StayAboveWaterTask(0.8f));
        tasks.add(new WalkTask(0.4f));
        tasks.add(new LookAroundTask(45, 90));
        tasks.add(new WanderAroundTask());
    }

    @Override
    protected void addIdleActivities(List<Task<? super FacehuggerEntityJava>> tasks) {
        tasks.add(new UpdateAttackTargetTask<>((it) -> true, this::getPreferredTarget));

        tasks.add(avoidRepellentTask());
        tasks.add(makeRandomWanderTask());
    }

    @Override
    protected void addAvoidActivities(List<Task<? super FacehuggerEntityJava>> tasks) {

    }

    @Override
    protected void addFightActivities(List<Task<? super FacehuggerEntityJava>> tasks) {
        tasks.add(new ForgetAttackTargetTask<>(it -> brain.hasMemoryModule(MemoryModuleType.NEAREST_REPELLENT) ||
                EntityUtils.isPotentialHost(it) ||
                ((Host) it).hasParasite() ||
                EntityUtils.isFacehuggerAttached(it)));
        tasks.add(new RangedApproachTask(1.0f));
        tasks.add(new FacehuggerPounceTaskJava());
    }

    private RandomTask<FacehuggerEntityJava> makeRandomWanderTask() {
        return new RandomTask<>(ImmutableList.of(
                Pair.of(new StrollTask(0.4f), 2),
                Pair.of(new WaitTask(30, 60), 1)
        ));
    }
}
