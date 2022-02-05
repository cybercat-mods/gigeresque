package com.bvanseg.gigeresque.common.entity.ai.brain

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.Activity
import net.minecraft.entity.ai.brain.Brain
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.task.GoToRememberedPositionTask
import net.minecraft.entity.ai.brain.task.Task
import net.minecraft.entity.mob.MobEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import java.util.*

/**
 * @author Boston Vanseghi
 */
abstract class ComplexBrain<T : LivingEntity>(protected val entity: T) {

    lateinit var brain: Brain<T>

    var timeStunned = 0

    open fun initialize(brain: Brain<out T>): Brain<out T> {
        val coreTasks = mutableListOf<Task<in T>>()
        addCoreActivities(coreTasks)

        val idleTasks = mutableListOf<Task<in T>>()
        addIdleActivities(idleTasks)

        val fightTasks = mutableListOf<Task<in T>>()
        addFightActivities(fightTasks)

        val avoidTasks = mutableListOf<Task<in T>>()
        addAvoidActivities(avoidTasks)

        brain.setTaskList(Activity.CORE, 0, ImmutableList.builder<Task<in T>>().addAll(coreTasks).build())
        brain.setTaskList(Activity.IDLE, 10, ImmutableList.builder<Task<in T>>().addAll(idleTasks).build())
        brain.setTaskList(
            Activity.FIGHT,
            10,
            ImmutableList.builder<Task<in T>>().addAll(fightTasks).build(),
            MemoryModuleType.ATTACK_TARGET
        )
        brain.setTaskList(
            Activity.AVOID,
            10,
            ImmutableList.builder<Task<in T>>().addAll(avoidTasks).build(),
            MemoryModuleType.AVOID_TARGET
        )

        brain.setCoreActivities(ImmutableSet.of(Activity.CORE))
        brain.setDefaultActivity(Activity.IDLE)
        brain.resetPossibleActivities()

        this.brain = brain as Brain<T>

        return brain
    }

    open fun tick() {
        if (timeStunned > 0) {
            timeStunned--
            return
        }

        brain.tick(entity.world as ServerWorld, entity)
    }

    open fun tickActivities() {
        brain.resetPossibleActivities(
            ImmutableList.of(
                Activity.FIGHT,
                Activity.AVOID,
                Activity.IDLE
            )
        )

        if (entity is MobEntity) {
            entity.isAttacking = brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET)
        }
    }

    protected abstract fun addCoreActivities(tasks: MutableList<Task<in T>>)

    protected open fun addIdleActivities(tasks: MutableList<Task<in T>>) = Unit
    protected open fun addAvoidActivities(tasks: MutableList<Task<in T>>) = Unit
    protected open fun addFightActivities(tasks: MutableList<Task<in T>>) = Unit

    /*
        UTIL FUNCTIONS
     */

    protected open fun getPreferredTarget(entity: T): Optional<out LivingEntity> {
        var entityOptional = brain.getOptionalMemory(MemoryModuleType.NEAREST_ATTACKABLE)

        if (entityOptional.isEmpty) {
            entityOptional = brain.getOptionalMemory(MemoryModuleType.ATTACK_TARGET)
        }
        return entityOptional
    }

    /*
        DEFAULT TASK HELPERS
     */

    protected open fun avoidRepellentTask(): GoToRememberedPositionTask<BlockPos> {
        return GoToRememberedPositionTask.toBlock(MemoryModuleType.NEAREST_REPELLENT, 1.0f, 8, false)
    }

    fun stun(duration: Int) {
        timeStunned = duration
    }
}