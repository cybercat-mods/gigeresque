package com.bvanseg.gigeresque.common.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity

/**
 * @author Boston Vanseghi
 */
interface Growable {
    var growth: Int
    val maxGrowth: Int

    fun grow(amount: Int)
    fun growInto(): LivingEntity?

    fun growUp(entity: LivingEntity) {
        val world = entity.world
        if (!world.isClient) {
            val newEntity = growInto() ?: return
            newEntity.refreshPositionAndAngles(entity.blockPos, entity.yaw, entity.pitch)
            world.spawnEntity(newEntity)
            entity.remove(Entity.RemovalReason.DISCARDED)
        }
    }

    fun getGrowthNeededUntilGrowUp(): Int = maxGrowth - growth
}