//package com.bvanseg.gigeresque.common.entity
//
//import net.minecraft.entity.Entity
//import net.minecraft.entity.LivingEntity
//import kotlin.math.min
//
///**
// * @author Boston Vanseghi
// */
//interface Growable {
//    var growth: Float
//    val maxGrowth: Float
//
//    fun grow(entity: LivingEntity, amount: Float) {
//        growth = min(growth + amount, maxGrowth)
//
//        if (growth >= maxGrowth) {
//            growUp(entity)
//        }
//    }
//
//    fun growInto(): LivingEntity?
//
//    fun growUp(entity: LivingEntity) {
//        val world = entity.world
//        if (!world.isClient) {
//            val newEntity = growInto() ?: return
//            newEntity.refreshPositionAndAngles(entity.blockPos, entity.yaw, entity.pitch)
//            world.spawnEntity(newEntity)
//            entity.remove(Entity.RemovalReason.DISCARDED)
//        }
//    }
//
//    fun getGrowthNeededUntilGrowUp(): Float = maxGrowth - growth
//
//    fun getGrowthMultiplier() = 1.0f
//}