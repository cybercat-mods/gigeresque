package com.bvanseg.gigeresque.common.entity.ai.brain.task;

import com.bvanseg.gigeresque.ConstantsJava;
import com.bvanseg.gigeresque.common.entity.AlienEntityJava;
import com.bvanseg.gigeresque.common.entity.impl.ChestbursterEntityJava;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;

import static java.lang.Math.ceil;
import static java.lang.Math.min;

public class ConsumeFoodItemTaskJava extends Task<ChestbursterEntityJava> {
    private double speed;
    public ConsumeFoodItemTaskJava(double speed) {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_ABSENT,
                MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleState.VALUE_PRESENT
        ));
        this.speed = speed;
    }

    @Override
    protected boolean shouldRun(ServerWorld world, ChestbursterEntityJava chestburster) {
        return chestburster.getBrain().hasMemoryModule(
                MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM
        );
    }

    @Override
    protected void run(ServerWorld serverWorld, ChestbursterEntityJava chestburster, long l) {
        var foodItemEntity =
                chestburster.getBrain().getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM).orElse(null);
        if (foodItemEntity == null) return;
        if (foodItemEntity.distanceTo(chestburster) <= getDesiredDistanceToTarget()) {
            var world = chestburster.world;

            if (chestburster.isAlive() && foodItemEntity.isAlive() && !world.isClient) {

                int growthLeft = (int) ceil(chestburster.getGrowthNeededUntilGrowUp() / (ConstantsJava.TPM * 2.0));
                int amountToEat = min(foodItemEntity.getStack().getCount(), growthLeft);

                foodItemEntity.getStack().decrement(amountToEat);
                chestburster.playSound(chestburster.getEatSound(foodItemEntity.getStack()), 1.0f, 1.0f);

                chestburster.grow(chestburster, amountToEat * ConstantsJava.TPM * 2.0f);
                chestburster.getBrain().forget(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
            }
        } else {
            startMovingToTarget(chestburster, foodItemEntity);
        }
    }

    private double getDesiredDistanceToTarget() {
        return 1.5;
    }

    private void startMovingToTarget(AlienEntityJava alien, ItemEntity targetEntity) {
        alien.getNavigation().startMovingTo(targetEntity, speed);
    }
}
