//package com.bvanseg.gigeresque.common.entity.ai.brain.sensor
//
//import com.bvanseg.gigeresque.common.entity.AlienEntity
//import com.google.common.collect.ImmutableSet
//import net.minecraft.entity.LivingEntity
//import net.minecraft.entity.ai.brain.LivingTargetCache
//import net.minecraft.entity.ai.brain.MemoryModuleType
//import net.minecraft.entity.ai.brain.sensor.Sensor
//import net.minecraft.entity.player.PlayerEntity
//import net.minecraft.server.world.ServerWorld
//
///**
// * @author Boston Vanseghi
// */
//class NearestLargerThreatSensor : Sensor<LivingEntity>() {
//
//    override fun getOutputMemoryModules(): Set<MemoryModuleType<*>> {
//        return ImmutableSet.of(
//            MemoryModuleType.AVOID_TARGET, MemoryModuleType.VISIBLE_MOBS
//        )
//    }
//
//    override fun sense(world: ServerWorld, entity: LivingEntity) {
//        val brain = entity.brain
//
//        val nearestVisibleMobs =
//            brain.getOptionalMemory(MemoryModuleType.VISIBLE_MOBS).orElse(LivingTargetCache.empty())
//
//        nearestVisibleMobs.findFirst {
//            if (it is AlienEntity) return@findFirst false
//
//            if (it.isSpectator || (it is PlayerEntity && it.isCreative)) {
//                false
//            } else {
//                it.height * it.width > entity.height * entity.width
//            }
//        }.ifPresent {
//            brain.remember(MemoryModuleType.AVOID_TARGET, it)
//        }
//    }
//}