package mods.cybercat.gigeresque.common.entity.ai.brain.task;

import static java.lang.Math.ceil;
import static java.lang.Math.min;

import com.google.common.collect.ImmutableMap;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.ChestbursterEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;

public class ConsumeFoodItemTask extends Task<ChestbursterEntity> {
	private double speed;

	public ConsumeFoodItemTask(double speed) {
		super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
				MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_ABSENT,
				MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleState.VALUE_PRESENT));
		this.speed = speed;
	}

	@Override
	protected boolean shouldRun(ServerWorld world, ChestbursterEntity chestburster) {
		return chestburster.getBrain().hasMemoryModule(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
	}

	@Override
	protected void run(ServerWorld serverWorld, ChestbursterEntity chestburster, long l) {
		var foodItemEntity = chestburster.getBrain().getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM)
				.orElse(null);
		if (foodItemEntity == null) {
			chestburster.setEatingStatus(false);
			return;
		}
		if (foodItemEntity.distanceTo(chestburster) <= getDesiredDistanceToTarget()) {
			var world = chestburster.world;

			if (chestburster.isAlive() && foodItemEntity.isAlive() && !world.isClient) {

				int growthLeft = (int) ceil(chestburster.getGrowthNeededUntilGrowUp() / (Constants.TPM * 2.0));
				int amountToEat = min(foodItemEntity.getStack().getCount(), growthLeft);

				foodItemEntity.getStack().decrement(amountToEat);
				chestburster.setEatingStatus(true);
				chestburster.playSound(chestburster.getEatSound(foodItemEntity.getStack()), 1.0f, 1.0f);

				chestburster.grow(chestburster, amountToEat * Constants.TPM * 2.0f);
				chestburster.getBrain().forget(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
			}
		} else {
			chestburster.setEatingStatus(false);
			startMovingToTarget(chestburster, foodItemEntity);
		}
	}

	private double getDesiredDistanceToTarget() {
		return 1.5;
	}

	private void startMovingToTarget(AlienEntity alien, ItemEntity targetEntity) {
		alien.getNavigation().startMovingTo(targetEntity, speed);
	}
}
