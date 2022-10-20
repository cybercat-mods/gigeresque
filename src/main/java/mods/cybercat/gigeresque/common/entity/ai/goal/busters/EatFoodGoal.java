package mods.cybercat.gigeresque.common.entity.ai.goal.busters;

import static java.lang.Math.ceil;
import static java.lang.Math.min;

import java.util.EnumSet;
import java.util.List;

import mods.cybercat.gigeresque.common.entity.impl.ChestbursterEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;

public class EatFoodGoal extends Goal {
	protected final ChestbursterEntity chestburster;
	private int cooldown = -1;

	public EatFoodGoal(ChestbursterEntity chestBurster) {
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		this.chestburster = chestBurster;
	}

	@Override
	public boolean canUse() {
		List<ItemEntity> list = this.chestburster.level.getEntitiesOfClass(ItemEntity.class,
				this.chestburster.getBoundingBox().inflate(8.0, 8.0, 8.0), ChestbursterEntity.PICKABLE_DROP_FILTER);
		return !list.isEmpty() && chestburster.tickCount > 80;
	}

	@Override
	public void tick() {
		List<ItemEntity> list = this.chestburster.level.getEntitiesOfClass(ItemEntity.class,
				this.chestburster.getBoundingBox().inflate(8.0, 8.0, 8.0), ChestbursterEntity.PICKABLE_DROP_FILTER);

		int growthLeft = (int) ceil(chestburster.getGrowthNeededUntilGrowUp() / (2400));
		int amountToEat = min(list.get(0).getItem().getCount(), growthLeft);
		if (!list.isEmpty()) {
			this.chestburster.getNavigation().moveTo(list.get(0), 1.2f);
		}
		this.chestburster.getNavigation().moveTo(list.get(0), 1.2f);
		if (list.get(0).distanceTo(chestburster) >= 0.1 && list.get(0).isAlive() && chestburster.tickCount > 80) {
			this.cooldown++;
			if (this.cooldown == 1) {
				this.chestburster.setEatingStatus(true);
			}
			if (this.cooldown == 15) {
				list.get(0).getItem().shrink(amountToEat);
				chestburster.playSound(chestburster.getEatingSound(list.get(0).getItem()), 1.0f, 1.0f);

				chestburster.grow(chestburster, amountToEat * 1200 * 2.0f);
			}
			if (this.cooldown >= 17) {
				this.chestburster.setEatingStatus(false);
				cooldown = -5;
			}
		}
	}

	@Override
	public void start() {
		super.start();
		List<ItemEntity> list = this.chestburster.level.getEntitiesOfClass(ItemEntity.class,
				this.chestburster.getBoundingBox().inflate(8.0, 8.0, 8.0), ChestbursterEntity.PICKABLE_DROP_FILTER);
		if (!list.isEmpty()) {
			this.chestburster.getNavigation().moveTo(list.get(0), 1.2f);
		}
		this.cooldown = 0;
	}

	@Override
	public void stop() {
		super.stop();
		cooldown = 0;
		this.chestburster.setEatingStatus(false);
	}
}