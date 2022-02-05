package com.bvanseg.gigeresque.common.entity.ai.brain

import com.bvanseg.gigeresque.common.entity.ai.brain.task.FacehuggerPounceTask
import com.bvanseg.gigeresque.common.entity.impl.FacehuggerEntity
import com.bvanseg.gigeresque.common.extensions.isFacehuggerAttached
import com.bvanseg.gigeresque.common.extensions.isPotentialHost
import com.bvanseg.gigeresque.interfacing.Host
import com.google.common.collect.ImmutableList
import com.mojang.datafixers.util.Pair
import java.util.function.Predicate
import net.minecraft.entity.ai.brain.Activity
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.task.ForgetAttackTargetTask
import net.minecraft.entity.ai.brain.task.LookAroundTask
import net.minecraft.entity.ai.brain.task.RandomTask
import net.minecraft.entity.ai.brain.task.RangedApproachTask
import net.minecraft.entity.ai.brain.task.StayAboveWaterTask
import net.minecraft.entity.ai.brain.task.StrollTask
import net.minecraft.entity.ai.brain.task.Task
import net.minecraft.entity.ai.brain.task.UpdateAttackTargetTask
import net.minecraft.entity.ai.brain.task.WaitTask
import net.minecraft.entity.ai.brain.task.WalkTask
import net.minecraft.entity.ai.brain.task.WanderAroundTask

/**
 * @author Boston Vanseghi
 */
class FacehuggerBrain(entity: FacehuggerEntity): ComplexBrain<FacehuggerEntity>(entity) {

    override fun addCoreActivities(tasks: MutableList<Task<in FacehuggerEntity>>) {
        tasks.add(StayAboveWaterTask(0.8f))
        tasks.add(WalkTask(0.4f))
        tasks.add(LookAroundTask(45, 90))
        tasks.add(WanderAroundTask())
    }

    override fun addIdleActivities(tasks: MutableList<Task<in FacehuggerEntity>>) {
        tasks.add(UpdateAttackTargetTask(Predicate {
            return@Predicate true
        }, this::getPreferredTarget))

        tasks.add(avoidRepellentTask())
        tasks.add(makeRandomWanderTask())
    }

    override fun addAvoidActivities(tasks: MutableList<Task<in FacehuggerEntity>>) = Unit

    override fun addFightActivities(tasks: MutableList<Task<in FacehuggerEntity>>) {
        tasks.add(ForgetAttackTargetTask(Predicate {
            return@Predicate brain.hasMemoryModule(MemoryModuleType.NEAREST_REPELLENT) ||
                    !it.isPotentialHost() ||
                    (it as Host).hasParasite() ||
                    it.isFacehuggerAttached()
        }))
        tasks.add(RangedApproachTask(1.0f))
        tasks.add(FacehuggerPounceTask())
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

    private fun makeRandomWanderTask(): RandomTask<FacehuggerEntity> {
        return RandomTask(
            ImmutableList.of(
                Pair.of<Task<in FacehuggerEntity>, Int>(StrollTask(0.4f), 2),
                Pair.of<Task<in FacehuggerEntity>, Int>(WaitTask(30, 60), 1)
            )
        )
    }
}