package com.bvanseg.gigeresque.common.entity.ai.brain

import com.bvanseg.gigeresque.common.entity.ai.brain.task.BuildNestTask
import com.bvanseg.gigeresque.common.entity.ai.brain.task.FindNestingGroundTask
import com.bvanseg.gigeresque.common.entity.impl.RunnerAlienEntity
import com.bvanseg.gigeresque.common.extensions.isFacehuggerAttached
import com.bvanseg.gigeresque.interfacing.Host
import com.google.common.collect.ImmutableList
import com.mojang.datafixers.util.Pair
import net.minecraft.entity.ai.brain.Activity
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.task.*
import java.util.function.Predicate

/**
 * @author Boston Vanseghi
 */
class RunnerAlienBrain(entity: RunnerAlienEntity): ComplexBrain<RunnerAlienEntity>(entity) {

    override fun addCoreActivities(tasks: MutableList<Task<in RunnerAlienEntity>>) {
        tasks.add(StayAboveWaterTask(0.8f))
        tasks.add(WalkTask(2.0f))
        tasks.add(LookAroundTask(45, 90))
        tasks.add(FindNestingGroundTask(2.0))
        tasks.add(BuildNestTask())
        tasks.add(WanderAroundTask())
    }

    override fun addIdleActivities(tasks: MutableList<Task<in RunnerAlienEntity>>) {
        tasks.add(UpdateAttackTargetTask(Predicate {
            return@Predicate true
        }, this::getPreferredTarget))

        tasks.add(avoidRepellentTask())
        tasks.add(makeRandomWanderTask())
    }

    override fun addAvoidActivities(tasks: MutableList<Task<in RunnerAlienEntity>>) = Unit

    override fun addFightActivities(tasks: MutableList<Task<in RunnerAlienEntity>>) {
        tasks.add(ForgetAttackTargetTask(Predicate {
            return@Predicate brain.hasMemoryModule(MemoryModuleType.NEAREST_REPELLENT) ||
                    (it as Host).hasParasite() ||
                    it.isFacehuggerAttached()
        }))
        tasks.add(RangedApproachTask(3.0f))
        tasks.add(MeleeAttackTask(20))
    }

    override fun tickActivities() {
        brain.resetPossibleActivities(
            ImmutableList.of(
                Activity.FIGHT,
                Activity.AVOID,
                Activity.IDLE
            )
        )
        entity.isAttacking = brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET)
    }

    private fun makeRandomWanderTask(): RandomTask<RunnerAlienEntity> {
        return RandomTask(
            ImmutableList.of(
                Pair.of<Task<in RunnerAlienEntity>, Int>(StrollTask(1.0f), 2),
                Pair.of<Task<in RunnerAlienEntity>, Int>(WaitTask(60, 120), 1)
            )
        )
    }
}