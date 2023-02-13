package mods.cybercat.gigeresque.common.entity.ai.tasks;

import java.util.List;
import java.util.SplittableRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.config.ConfigAccessor;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.ClassicAlienEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

public class ClassicXenoMeleeAttackTask<E extends ClassicAlienEntity> extends DelayedBehaviour<E> {
	private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
			Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT),
			Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT));

	protected Function<E, Integer> attackIntervalSupplier = entity -> 20;
	public static final Predicate<BlockState> NEST = state -> state.is(GIgBlocks.NEST_RESIN_WEB_CROSS);

	@Nullable
	protected LivingEntity target = null;

	public ClassicXenoMeleeAttackTask(int delayTicks) {
		super(delayTicks);
	}

	/**
	 * Set the time between attacks.
	 * 
	 * @param supplier The tick value provider
	 * @return this
	 */
	public ClassicXenoMeleeAttackTask<E> attackInterval(Function<E, Integer> supplier) {
		this.attackIntervalSupplier = supplier;

		return this;
	}

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
		this.target = BrainUtils.getTargetOfEntity(entity);
		Stream<BlockState> list2 = target.level.getBlockStatesIfLoaded(target.getBoundingBox().inflate(2.0, 2.0, 2.0));

		return entity.getSensing().hasLineOfSight(this.target) && entity.isWithinMeleeAttackRange(this.target)
				&& entity.getEntityData().get(AlienEntity.FLEEING_FIRE) != true && !list2.anyMatch(NEST);
	}

	@Override
	protected void start(E entity) {
		entity.swing(InteractionHand.MAIN_HAND);
		BehaviorUtils.lookAtEntity(entity, this.target);
	}

	@Override
	protected void stop(E entity) {
		this.target = null;
	}

	@Override
	protected void doDelayedAction(E entity) {
		BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true,
				this.attackIntervalSupplier.apply(entity));

		if (this.target == null)
			return;

		if (!entity.getSensing().hasLineOfSight(this.target) || !entity.isWithinMeleeAttackRange(this.target))
			return;

		var list = entity.level.getBlockStatesIfLoaded(entity.getBoundingBox().inflate(18.0, 18.0, 18.0));
		var list2 = target.level.getBlockStatesIfLoaded(target.getBoundingBox().inflate(2.0, 2.0, 2.0));
		var random = new SplittableRandom();
		var randomPhase = random.nextInt(0, 100);
		if ((list.anyMatch(NEST) && randomPhase >= 50) && !list2.anyMatch(NEST)
				&& ConfigAccessor.isTargetHostable(target)) {
			entity.grabTarget(target);
		} else {
			if (!entity.isVehicle()) {
				entity.doHurtTarget(target);
			}
		}
	}
}
