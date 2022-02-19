//package com.bvanseg.gigeresque.common.entity.ai.brain
//
//import com.bvanseg.gigeresque.common.entity.ai.brain.task.FacehuggerPounceTask
//import com.bvanseg.gigeresque.common.entity.impl.FacehuggerEntity
//import com.bvanseg.gigeresque.common.extensions.isFacehuggerAttached
//import com.bvanseg.gigeresque.common.extensions.isPotentialHost
//import com.bvanseg.gigeresque.interfacing.Host
//import com.google.common.collect.ImmutableList
//import com.mojang.datafixers.util.Pair
//import net.minecraft.entity.ai.brain.MemoryModuleType
//import net.minecraft.entity.ai.brain.task.*
//import java.util.function.Predicate
//
///**
// * @author Boston Vanseghi
// */
//class FacehuggerBrain(entity: FacehuggerEntity) : ComplexBrain<FacehuggerEntity>(entity) {
//
//    override fun addCoreActivities(tasks: MutableList<Task<in FacehuggerEntity>>) {
//        tasks.add(StayAboveWaterTask(0.8f))
//        tasks.add(WalkTask(0.4f))
//        tasks.add(LookAroundTask(45, 90))
//        tasks.add(WanderAroundTask())
//    }
//
//    override fun addIdleActivities(tasks: MutableList<Task<in FacehuggerEntity>>) {
//        tasks.add(UpdateAttackTargetTask(Predicate {
//            return@Predicate true
//        }, this::getPreferredTarget))
//
//        tasks.add(avoidRepellentTask())
//        tasks.add(makeRandomWanderTask())
//    }
//
//    override fun addAvoidActivities(tasks: MutableList<Task<in FacehuggerEntity>>) = Unit
//
//    override fun addFightActivities(tasks: MutableList<Task<in FacehuggerEntity>>) {
//        tasks.add(ForgetAttackTargetTask(Predicate {
//            return@Predicate brain.hasMemoryModule(MemoryModuleType.NEAREST_REPELLENT) ||
//                    !it.isPotentialHost() ||
//                    (it as Host).hasParasite() ||
//                    it.isFacehuggerAttached()
//        }))
//        tasks.add(RangedApproachTask(1.0f))
//        tasks.add(FacehuggerPounceTask())
//    }
//
//    private fun makeRandomWanderTask(): RandomTask<FacehuggerEntity> {
//        return RandomTask(
//            ImmutableList.of(
//                Pair.of<Task<in FacehuggerEntity>, Int>(StrollTask(0.4f), 2),
//                Pair.of<Task<in FacehuggerEntity>, Int>(WaitTask(30, 60), 1)
//            )
//        )
//    }
//}