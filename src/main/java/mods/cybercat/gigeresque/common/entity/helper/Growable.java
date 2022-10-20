package mods.cybercat.gigeresque.common.entity.helper;

import static java.lang.Math.min;

import mods.cybercat.gigeresque.common.entity.impl.RunnerbursterEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public interface Growable {
	float getGrowth();

	void setGrowth(float growth);

	float getMaxGrowth();

	default void grow(LivingEntity entity, float amount) {
		setGrowth(min(getGrowth() + amount, getMaxGrowth()));

		if (getGrowth() >= getMaxGrowth()) {
			growUp(entity);
		}
	}

	LivingEntity growInto();

	default void growUp(LivingEntity entity) {
		Level world = entity.level;
		if (!world.isClientSide()) {
			var newEntity = growInto();
			if (newEntity == null)
				return;
			newEntity.moveTo(entity.blockPosition(), entity.getYRot(), entity.getXRot());
			if (newEntity instanceof RunnerbursterEntity)
					((RunnerbursterEntity) newEntity).setBirthStatus(false);
			world.addFreshEntity(newEntity);
			entity.remove(Entity.RemovalReason.DISCARDED);
		}
	}

	default float getGrowthNeededUntilGrowUp() {
		return getMaxGrowth() - getGrowth();
	}

	default float getGrowthMultiplier() {
		return 1.0f;
	}
}
