//package com.bvanseg.gigeresque.common.entity.ai.brain
//
//import com.bvanseg.gigeresque.common.entity.ai.brain.task.ConsumeFoodItemTask
//import com.bvanseg.gigeresque.common.entity.impl.AquaticChestbursterEntity
//import com.bvanseg.gigeresque.common.entity.impl.ChestbursterEntity
//import com.bvanseg.gigeresque.common.entity.impl.RunnerbursterEntity
//import com.google.common.collect.ImmutableList
//import com.mojang.datafixers.util.Pair
//import net.minecraft.entity.ai.brain.Activity
//import net.minecraft.entity.ai.brain.MemoryModuleType
//import net.minecraft.entity.ai.brain.task.*
//import java.util.function.Predicate
//
///**
// * @author Boston Vanseghi
// */
//open class ChestbursterBrain(entity: ChestbursterEntity) : ComplexBrain<ChestbursterEntity>(entity) {
//
//    override fun addCoreActivities(tasks: MutableList<Task<in ChestbursterEntity>>) {
//        if (entity !is AquaticChestbursterEntity) {
//            tasks.add(StayAboveWaterTask(0.8f))
//        }
//        tasks.add(WalkTask(if (entity is RunnerbursterEntity) 1.0f else 2.0f))
//        tasks.add(LookAroundTask(45, 90))
//        tasks.add(WanderAroundTask())
//    }
//
//    override fun addIdleActivities(tasks: MutableList<Task<in ChestbursterEntity>>) {
//        tasks.add(UpdateAttackTargetTask(Predicate {
//            return@Predicate true
//        }, this::getPreferredTarget))
//
//        tasks.add(ConsumeFoodItemTask(1.0))
//        tasks.add(avoidRepellentTask())
//        tasks.add(makeRandomWanderTask())
//    }
//
//    override fun addAvoidActivities(tasks: MutableList<Task<in ChestbursterEntity>>) {
//        tasks.add(
//            GoToRememberedPositionTask.toEntity(
//                MemoryModuleType.AVOID_TARGET,
//                if (entity is RunnerbursterEntity) 1.5f else 1.0f, 12, true
//            )
//        )
//        tasks.add(makeRandomWanderTask())
//        tasks.add(ForgetTask(Predicate { chestburster ->
//            if (brain.hasMemoryModule(MemoryModuleType.AVOID_TARGET)) {
//                val avoidTarget = brain.getOptionalMemory(MemoryModuleType.AVOID_TARGET).get()
//
//                if (avoidTarget.isDead ||
//                    avoidTarget.height * avoidTarget.width < (chestburster.height * chestburster.width) * 3 ||
//                    chestburster.distanceTo(avoidTarget) > 12
//                ) {
//                    return@Predicate true
//                }
//            }
//            return@Predicate false
//        }, MemoryModuleType.AVOID_TARGET))
//    }
//
//    override fun addFightActivities(tasks: MutableList<Task<in ChestbursterEntity>>) {
//        tasks.add(ForgetAttackTargetTask(Predicate {
//            return@Predicate brain.hasMemoryModule(MemoryModuleType.NEAREST_REPELLENT) ||
//                    brain.hasMemoryModule(MemoryModuleType.AVOID_TARGET)
//        }))
//        tasks.add(RangedApproachTask(if (entity is RunnerbursterEntity) 1.5f else 1.0f))
//        tasks.add(MeleeAttackTask(20))
//    }
//
//    override fun tickActivities() {
//        brain.resetPossibleActivities(
//            ImmutableList.of(
//                Activity.FIGHT,
//                Activity.AVOID,
//                Activity.IDLE
//            )
//        )
//        entity.isAttacking = brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET)
//    }
//
//    private fun makeRandomWanderTask(): RandomTask<ChestbursterEntity> {
//        return RandomTask(
//            ImmutableList.of(
//                Pair.of<Task<in ChestbursterEntity>, Int>(StrollTask(1.0f), 2),
//                Pair.of<Task<in ChestbursterEntity>, Int>(WaitTask(60, 120), 1)
//            )
//        )
//    }
//}