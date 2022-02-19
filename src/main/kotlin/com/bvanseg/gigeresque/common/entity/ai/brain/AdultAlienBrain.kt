//package com.bvanseg.gigeresque.common.entity.ai.brain
//
//import com.bvanseg.gigeresque.common.entity.AlienEntity
//import com.bvanseg.gigeresque.common.entity.ai.brain.task.*
//import com.bvanseg.gigeresque.common.entity.attribute.AlienEntityAttributes
//import com.bvanseg.gigeresque.common.entity.impl.AdultAlienEntity
//import com.bvanseg.gigeresque.common.entity.impl.AquaticAlienEntity
//import com.bvanseg.gigeresque.common.extensions.isEggmorphable
//import com.bvanseg.gigeresque.common.extensions.isFacehuggerAttached
//import com.bvanseg.gigeresque.interfacing.Eggmorphable
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
//class AdultAlienBrain(entity: AdultAlienEntity) : ComplexBrain<AdultAlienEntity>(entity) {
//
//    private val intelligence = entity.attributes.getValue(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE)
//
//    private val isAquatic: Boolean
//        get() = entity is AquaticAlienEntity
//
//    private val aquaticLandPenalty = if (isAquatic && !entity.isTouchingWater) 0.5f else 1.0f
//
//    override fun addCoreActivities(tasks: MutableList<Task<in AdultAlienEntity>>) {
//        if (!isAquatic) {
//            tasks.add(StayAboveWaterTask(0.8f))
//            tasks.add(FindNestingGroundTask(2.0))
//            tasks.add(BuildNestTask())
//            tasks.add(PickUpEggmorphableTargetTask(3.0))
//            tasks.add(EggmorphTargetTask(3.0))
//        }
//
//        tasks.add(WalkTask(2.0f * aquaticLandPenalty))
//        tasks.add(LookAroundTask(45, 90))
//        tasks.add(WanderAroundTask())
//    }
//
//    override fun addIdleActivities(tasks: MutableList<Task<in AdultAlienEntity>>) {
//        tasks.add(UpdateAttackTargetTask(Predicate {
//            return@Predicate true
//        }, this::getPreferredTarget))
//
//        if (intelligence >= AlienEntityAttributes.SABOTAGE_THRESHOLD) {
//            tasks.add(DestroyLightTask((2.0 * aquaticLandPenalty) + (2 * (intelligence / 0.85))))
//        }
//
//        if (intelligence >= AlienEntityAttributes.SELF_PRESERVE_THRESHOLD) {
//            tasks.add(avoidRepellentTask())
//        }
//
//        tasks.add(makeRandomWanderTask())
//    }
//
//    override fun addAvoidActivities(tasks: MutableList<Task<in AdultAlienEntity>>) = Unit
//
//    override fun addFightActivities(tasks: MutableList<Task<in AdultAlienEntity>>) {
//        tasks.add(ForgetAttackTargetTask(Predicate {
//            return@Predicate (brain.hasMemoryModule(MemoryModuleType.NEAREST_REPELLENT) && intelligence >= AlienEntityAttributes.SELF_PRESERVE_THRESHOLD) ||
//                    (it as Host).hasParasite() ||
//                    it.isFacehuggerAttached() ||
//                    (entity.brain.hasMemoryModule(MemoryModuleType.HOME) && it.isEggmorphable()) ||
//                    (it as Eggmorphable).isEggmorphing ||
//                    it.vehicle != null && it.vehicle is AlienEntity
//        }))
//        tasks.add(RangedApproachTask(3.0f * aquaticLandPenalty))
//        tasks.add(MeleeAttackTask(20 * intelligence.toInt()))
//    }
//
//    private fun makeRandomWanderTask(): RandomTask<AdultAlienEntity> {
//        return RandomTask<AdultAlienEntity>(
//            ImmutableList.of<Pair<Task<in AdultAlienEntity>, Int>>(
//                Pair.of<Task<in AdultAlienEntity>, Int>(StrollTask(1.0f), 2),
//                Pair.of<Task<in AdultAlienEntity>, Int>(WaitTask(60, 120), 1)
//            )
//        )
//    }
//}