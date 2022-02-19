//package com.bvanseg.gigeresque.common.entity.ai.brain
//
//import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypes
//import com.bvanseg.gigeresque.common.entity.impl.AlienEggEntity
//import com.google.common.collect.ImmutableList
//import net.minecraft.entity.ai.brain.Activity
//import net.minecraft.entity.ai.brain.MemoryModuleType
//import net.minecraft.entity.ai.brain.task.Task
//import net.minecraft.entity.ai.brain.task.UpdateAttackTargetTask
//import java.util.function.Predicate
//
///**
// * @author Boston Vanseghi
// */
//class AlienEggBrain(entity: AlienEggEntity) : ComplexBrain<AlienEggEntity>(entity) {
//
//    override fun addCoreActivities(tasks: MutableList<Task<in AlienEggEntity>>) = Unit
//
//    override fun addIdleActivities(tasks: MutableList<Task<in AlienEggEntity>>) {
//        tasks.add(UpdateAttackTargetTask(Predicate {
//            return@Predicate true
//        }, this::getPreferredTarget))
//    }
//
//    override fun addAvoidActivities(tasks: MutableList<Task<in AlienEggEntity>>) = Unit
//    override fun addFightActivities(tasks: MutableList<Task<in AlienEggEntity>>) = Unit
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
//
//        tryHatch()
//    }
//
//    private fun tryHatch() {
//        val nearestEggs = brain.getOptionalMemory(MemoryModuleTypes.NEAREST_EGGS).orElseGet { emptyList() }
//        val nearestFacehuggers = brain.getOptionalMemory(MemoryModuleTypes.NEAREST_FACEHUGGERS)
//            .orElseGet { emptyList() }.filter { it.ticksAttachedToHost < 0 }
//        val nearestHosts = brain.getOptionalMemory(MemoryModuleTypes.NEAREST_HOSTS).orElseGet { emptyList() }
//        val nearestHatchingEggs = nearestEggs.filter { it.isHatching || (it.isHatched && it.hasFacehugger) }
//
//        // The egg can hatch if there are more hosts than facehuggers.
//        if (nearestHosts.size > (nearestFacehuggers.size + nearestHatchingEggs.size)) {
//            entity.isHatching = true
//        }
//    }
//}