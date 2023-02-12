package mods.cybercat.gigeresque.common.entity.ai.tasks;

import static java.lang.Math.ceil;
import static java.lang.Math.min;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.cybercat.gigeresque.common.entity.ai.GigMemoryTypes;
import mods.cybercat.gigeresque.common.entity.impl.ChestbursterEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

public class EatFoodTask<E extends ChestbursterEntity> extends ExtendedBehaviour<E> {

	private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList
			.of(Pair.of(GigMemoryTypes.FOOD_ITEMS.get(), MemoryStatus.VALUE_PRESENT));
	protected final float speed;

	public EatFoodTask(float speed) {
		this.speed = speed;
	}

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	@Override
	protected void tick(E entity) {
		var lightSourceLocation = entity.getBrain().getMemory(GigMemoryTypes.FOOD_ITEMS.get()).orElse(null);
		var growthLeft = (int) ceil(entity.getGrowthNeededUntilGrowUp() / (2400));
		if (lightSourceLocation == null)
			return;
		var amountToEat = min(lightSourceLocation.stream().findFirst().get().getItem().getCount(), growthLeft);
		if (!lightSourceLocation.stream().findFirst().get().blockPosition().closerToCenterThan(entity.position(), 2.4))
			startMovingToTarget(entity, lightSourceLocation.stream().findFirst().get().blockPosition());
		else {
			lightSourceLocation.stream().findFirst().get().getItem().shrink(amountToEat);
			entity.playSound(entity.getEatingSound(lightSourceLocation.stream().findFirst().get().getItem()), 1.0f,
					1.0f);
			entity.grow(entity, amountToEat * 1200 * 2.0f);
		}
	}

	private void startMovingToTarget(E alien, BlockPos targetPos) {
		alien.getNavigation().moveTo(((double) ((float) targetPos.getX())) + 0.5, targetPos.getY(),
				((double) ((float) targetPos.getZ())) + 0.5, 2.5F);
	}

}
