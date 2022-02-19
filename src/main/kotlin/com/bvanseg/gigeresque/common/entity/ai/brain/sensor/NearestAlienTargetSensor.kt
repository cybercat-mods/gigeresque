//package com.bvanseg.gigeresque.common.entity.ai.brain.sensor
//
//import com.bvanseg.gigeresque.common.config.ConfigAccessor
//import com.bvanseg.gigeresque.common.entity.AlienEntity
//import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypes
//import com.bvanseg.gigeresque.common.extensions.getOrNull
//import com.bvanseg.gigeresque.common.extensions.isEggmorphable
//import com.bvanseg.gigeresque.interfacing.Eggmorphable
//import com.bvanseg.gigeresque.interfacing.Host
//import com.google.common.collect.ImmutableSet
//import net.minecraft.entity.EntityGroup
//import net.minecraft.entity.LivingEntity
//import net.minecraft.entity.ai.brain.LivingTargetCache
//import net.minecraft.entity.ai.brain.MemoryModuleType
//import net.minecraft.entity.ai.brain.sensor.Sensor
//import net.minecraft.entity.mob.AmbientEntity
//import net.minecraft.server.world.ServerWorld
//
///**
// * @author Boston Vanseghi
// */
//class NearestAlienTargetSensor : Sensor<LivingEntity>() {
//
//    override fun getOutputMemoryModules(): Set<MemoryModuleType<*>> {
//        return ImmutableSet.of(
//            MemoryModuleTypes.EGGMORPH_TARGET,
//            MemoryModuleType.NEAREST_ATTACKABLE,
//            MemoryModuleType.VISIBLE_MOBS,
//            MemoryModuleType.NEAREST_VISIBLE_PLAYER
//        )
//    }
//
//    override fun sense(world: ServerWorld, entity: LivingEntity) {
//        val brain = entity.brain
//
//        // Prioritize players
//        val nearestPlayer = brain.getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER)
//
//        nearestPlayer.getOrNull()?.let { player ->
//            if (!(player as Host).hasParasite()) {
//                brain.remember(MemoryModuleType.NEAREST_ATTACKABLE, player)
//                return
//            }
//        }
//
//        val nearestVisibleMobs: LivingTargetCache =
//            brain.getOptionalMemory(MemoryModuleType.VISIBLE_MOBS).orElse(LivingTargetCache.empty())
//
//        nearestVisibleMobs.findFirst {
//            if (entity.brain.hasMemoryModule(MemoryModuleType.HOME) && it.isEggmorphable() && !it.hasVehicle()) {
//                brain.remember(MemoryModuleTypes.EGGMORPH_TARGET, it)
//                return@findFirst true
//            }
//            if (ConfigAccessor.isTargetBlacklisted(entity, it)) return@findFirst false
//            if (ConfigAccessor.isTargetWhitelisted(entity, it)) return@findFirst true
//
//            it !is AlienEntity &&
//                    (it as Host).doesNotHaveParasite() &&
//                    (it as Eggmorphable).isNotEggmorphing &&
//                    it !is AmbientEntity && it.group != EntityGroup.UNDEAD && it.group != EntityGroup.AQUATIC
//        }.ifPresent {
//            brain.remember(MemoryModuleType.NEAREST_ATTACKABLE, it)
//        }
//    }
//}