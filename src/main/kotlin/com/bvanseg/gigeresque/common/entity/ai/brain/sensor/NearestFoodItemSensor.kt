package com.bvanseg.gigeresque.common.entity.ai.brain.sensor

import com.google.common.collect.ImmutableSet
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.sensor.Sensor
import net.minecraft.server.world.ServerWorld
import java.util.*

/**
 * @author Boston Vanseghi
 */
class NearestFoodItemSensor : Sensor<LivingEntity>() {

    override fun getOutputMemoryModules(): Set<MemoryModuleType<*>> {
        return ImmutableSet.of(
            MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM
        )
    }

    override fun sense(world: ServerWorld, entity: LivingEntity) {
        val brain = entity.brain

        val foodItems = entity.world.getEntitiesByClass(ItemEntity::class.java, entity.boundingBox.expand(4.0)) {
            it.stack.isFood
        }

        brain.remember(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, Optional.ofNullable(foodItems.firstOrNull()))
    }
}