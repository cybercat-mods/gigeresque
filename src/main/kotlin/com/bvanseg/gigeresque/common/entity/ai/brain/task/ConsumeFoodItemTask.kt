//package com.bvanseg.gigeresque.common.entity.ai.brain.task
//
//import com.bvanseg.gigeresque.Constants
//import com.bvanseg.gigeresque.common.entity.AlienEntity
//import com.bvanseg.gigeresque.common.entity.impl.ChestbursterEntity
//import com.bvanseg.gigeresque.common.extensions.getOrNull
//import com.google.common.collect.ImmutableMap
//import net.minecraft.entity.ItemEntity
//import net.minecraft.entity.ai.brain.MemoryModuleState
//import net.minecraft.entity.ai.brain.MemoryModuleType
//import net.minecraft.entity.ai.brain.task.Task
//import net.minecraft.server.world.ServerWorld
//import kotlin.math.ceil
//import kotlin.math.min
//
///**
// * @author Boston Vanseghi
// */
//class ConsumeFoodItemTask(private val speed: Double) : Task<ChestbursterEntity>(
//    ImmutableMap.of(
//        MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
//        MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_ABSENT,
//        MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleState.VALUE_PRESENT,
//    )
//) {
//
//    override fun shouldRun(serverWorld: ServerWorld, chestburster: ChestbursterEntity): Boolean =
//        chestburster.brain.hasMemoryModule(
//            MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM
//        )
//
//    override fun run(serverWorld: ServerWorld, chestburster: ChestbursterEntity, l: Long) {
//        val foodItemEntity =
//            chestburster.brain.getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM).getOrNull() ?: return
//
//        if (foodItemEntity.distanceTo(chestburster) <= getDesiredDistanceToTarget()) {
//            val world = chestburster.world
//
//            if (chestburster.isAlive && foodItemEntity.isAlive && !world.isClient) {
//
//                val growthLeft = ceil(chestburster.getGrowthNeededUntilGrowUp() / (Constants.TPM * 2.0)).toInt()
//                val amountToEat = min(foodItemEntity.stack.count, growthLeft)
//
//                foodItemEntity.stack.decrement(amountToEat)
//                chestburster.playSound(chestburster.getEatSound(foodItemEntity.stack), 1.0f, 1.0f)
//
//                chestburster.grow(chestburster, amountToEat * Constants.TPM * 2.0f)
//                chestburster.brain.forget(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM)
//            }
//        } else {
//            startMovingToTarget(chestburster, foodItemEntity)
//        }
//    }
//
//    private fun getDesiredDistanceToTarget(): Double = 1.5
//
//
//    private fun startMovingToTarget(alien: AlienEntity, targetEntity: ItemEntity) {
//        alien.navigation.startMovingTo(targetEntity, speed)
//    }
//}