package com.bvanseg.gigeresque.common.entity.ai.brain;

import com.bvanseg.gigeresque.common.entity.AlienEntityJava;
import com.bvanseg.gigeresque.common.entity.ai.brain.task.BuildNestTaskJava;
import com.bvanseg.gigeresque.common.entity.ai.brain.task.FindNestingGroundTaskJava;
import com.bvanseg.gigeresque.common.entity.attribute.AlienEntityAttributesJava;
import com.bvanseg.gigeresque.common.entity.impl.AdultAlienEntityJava;
import com.bvanseg.gigeresque.common.util.EntityUtils;
import com.bvanseg.gigeresque.interfacing.Eggmorphable;
import com.bvanseg.gigeresque.interfacing.Host;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;

import java.util.List;

public class AdultAlienBrainJava extends ComplexBrainJava<AdultAlienEntityJava> {
    private double intelligence = entity.getAttributes().getValue(AlienEntityAttributesJava.INTELLIGENCE_ATTRIBUTE);

    private boolean isAquatic() {
        return entity instanceof AquaticAlienEntityJava;
    }

    private float aquaticLandPenalty = (isAquatic() && !entity.isTouchingWater()) ? 0.5f : 1.0f;

    @Override
    protected void addCoreActivities(List<Task<? super AdultAlienEntityJava>> tasks) {
        if (!isAquatic()) {
            tasks.add(new StayAboveWaterTask(0.8f));
            tasks.add(new FindNestingGroundTaskJava(2.0));
            tasks.add(new BuildNestTaskJava());
            tasks.add(new PickUpEggmorphableTargetTaskJava(3.0));
            tasks.add(new EggmorphTargetTaskJava(3.0));
        }
        tasks.add(new WalkTask(2.0f * aquaticLandPenalty));
        tasks.add(new LookAroundTask(45, 90));
        tasks.add(new WanderAroundTask());
    }

    @Override
    protected void addIdleActivities(List<Task<? super AdultAlienEntityJava>> tasks) {
        tasks.add(new UpdateAttackTargetTask<>(it -> true, this::getPreferredTarget));

        if (intelligence >= AlienEntityAttributesJava.SABOTAGE_THRESHOLD) {
            tasks.add(new DestroyLightTaskJava((2.0 * aquaticLandPenalty) + (2 * (intelligence / 0.85))));
        }

        if (intelligence >= AlienEntityAttributesJava.SELF_PRESERVE_THRESHOLD) {
            tasks.add(new AvoidRepellentTaskJava());
        }

        tasks.add(makeRandomWanderTask());
    }

    @Override
    protected void addAvoidActivities(List<Task<? super AdultAlienEntityJava>> tasks) {
    }

    @Override
    protected void addFightActivities(List<Task<? super AdultAlienEntityJava>> tasks) {
        tasks.add(new ForgetAttackTargetTask<>(it -> (brain.hasMemoryModule(MemoryModuleType.NEAREST_REPELLENT) && intelligence >= AlienEntityAttributesJava.SELF_PRESERVE_THRESHOLD) ||
                ((Host) it).hasParasite() ||
                EntityUtils.isFacehuggerAttached(it) ||
                (entity.getBrain().hasMemoryModule(MemoryModuleType.HOME) && EntityUtils.isEggmorphable(it)) ||
                ((Eggmorphable) it).isEggmorphing() ||
                it.getVehicle() != null && it.getVehicle() instanceof AlienEntityJava));
        tasks.add(new RangedApproachTask(3.0f * aquaticLandPenalty));
        tasks.add(new MeleeAttackTask(20 * (int) intelligence));
    }

    private RandomTask<AdultAlienEntityJava> makeRandomWanderTask() {
        return new RandomTask<>(ImmutableList.of(Pair.of(new StrollTask(1.0f), 2), Pair.of(new WaitTask(60, 120), 1)));
    }
}
