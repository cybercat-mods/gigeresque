package mods.cybercat.gigeresque.common.entity.ai.tasks;

import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.cybercat.gigeresque.common.entity.ai.GigMemoryTypes;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

public class EatFoodTask<E extends ChestbursterEntity> extends DelayedBehaviour<E> {
	private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(GigMemoryTypes.FOOD_ITEMS.get(), MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT));

	protected Function<E, Integer> attackIntervalSupplier = entity -> 20;

	@Nullable
	protected LivingEntity target = null;

	public EatFoodTask(int delayTicks) {
		super(delayTicks);
	}

	public EatFoodTask<E> attackInterval(Function<E, Integer> supplier) {
		this.attackIntervalSupplier = supplier;

		return this;
	}

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, E entity) {

		return entity.isAlive();
	}

	@Override
	protected void start(E entity) {
		entity.setEatingStatus(false);
	}

	@Override
	protected void stop(E entity) {
		entity.setEatingStatus(false);
	}

	@Override
	protected void doDelayedAction(E entity) {
		BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.attackIntervalSupplier.apply(entity));
		if (entity.getBrain().getMemory(GigMemoryTypes.FOOD_ITEMS.get()).orElse(null) == null)
			return;
		var itemLocation = entity.getBrain().getMemory(GigMemoryTypes.FOOD_ITEMS.get()).orElse(null);
		if (itemLocation.stream().findFirst().get() == null)
			return;

		if (!itemLocation.stream().findFirst().get().blockPosition().closerToCenterThan(entity.position(), 1.2)) 
			BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(itemLocation.stream().findFirst().get().blockPosition(), 0.7F, 0));
		
		if (itemLocation.stream().findFirst().get().blockPosition().closerToCenterThan(entity.position(), 1.2)) {
			entity.getNavigation().stop();
			entity.setEatingStatus(true);
			entity.triggerAnim("attackController", "eat");
			itemLocation.stream().findFirst().get().getItem().finishUsingItem(entity.getLevel(), entity);
			itemLocation.stream().findFirst().get().getItem().shrink(1);
			entity.grow(entity, 2400.0f);
		}
	}
}