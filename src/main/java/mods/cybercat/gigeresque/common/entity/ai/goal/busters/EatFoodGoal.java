package mods.cybercat.gigeresque.common.entity.ai.goal.busters;

import static java.lang.Math.ceil;
import static java.lang.Math.min;

import java.util.EnumSet;
import java.util.List;

import mods.cybercat.gigeresque.common.entity.impl.ChestbursterEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;

public class EatFoodGoal extends Goal {
	protected final ChestbursterEntity chestburster;
	private int cooldown = -1;

	public EatFoodGoal(ChestbursterEntity chestBurster) {
		this.setControls(EnumSet.of(Goal.Control.MOVE));
		this.chestburster = chestBurster;
	}

	@Override
	public boolean canStart() {
		List<ItemEntity> list = this.chestburster.world.getEntitiesByClass(ItemEntity.class,
				this.chestburster.getBoundingBox().expand(8.0, 8.0, 8.0), ChestbursterEntity.PICKABLE_DROP_FILTER);
		return !list.isEmpty();
	}

	@Override
	public void tick() {
		List<ItemEntity> list = this.chestburster.world.getEntitiesByClass(ItemEntity.class,
				this.chestburster.getBoundingBox().expand(8.0, 8.0, 8.0), ChestbursterEntity.PICKABLE_DROP_FILTER);

		int growthLeft = (int) ceil(chestburster.getGrowthNeededUntilGrowUp() / (2400));
		int amountToEat = min(list.get(0).getStack().getCount(), growthLeft);
		if (!list.isEmpty()) {
			this.chestburster.getNavigation().startMovingTo(list.get(0), 1.2f);
		}
		this.chestburster.getNavigation().startMovingTo(list.get(0), 1.2f);
		if (list.get(0).distanceTo(chestburster) >= 0.1 && list.get(0).isAlive()) {
			this.cooldown++;
			if (this.cooldown == 1) {
				this.chestburster.setEatingStatus(true);
			}
			if (this.cooldown == 15) {
				list.get(0).getStack().decrement(amountToEat);
				chestburster.playSound(chestburster.getEatSound(list.get(0).getStack()), 1.0f, 1.0f);

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
		List<ItemEntity> list = this.chestburster.world.getEntitiesByClass(ItemEntity.class,
				this.chestburster.getBoundingBox().expand(8.0, 8.0, 8.0), ChestbursterEntity.PICKABLE_DROP_FILTER);
		if (!list.isEmpty()) {
			this.chestburster.getNavigation().startMovingTo(list.get(0), 1.2f);
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