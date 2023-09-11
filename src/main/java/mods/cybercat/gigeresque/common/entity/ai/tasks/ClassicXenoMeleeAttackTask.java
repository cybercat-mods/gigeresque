package mods.cybercat.gigeresque.common.entity.ai.tasks;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.util.BrainUtils;

public class ClassicXenoMeleeAttackTask<E extends ClassicAlienEntity> extends CustomDelayedMeleeBehaviour<E> {
	private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT));

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
		return !entity.isVehicle() && BrainUtils.canSee(entity, target) && entity.isWithinMeleeAttackRange(this.target) && !this.target.getCommandSenderWorld().getBlockStates(this.target.getBoundingBox().inflate(1)).anyMatch(NEST);
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
		BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.attackIntervalSupplier.apply(entity));

		if (this.target == null)
			return;

		if (!entity.getSensing().hasLineOfSight(this.target) || !entity.isWithinMeleeAttackRange(this.target))
			return;

		if (this.target.getCommandSenderWorld().getBlockStates(this.target.getBoundingBox().inflate(1)).anyMatch(state -> state.is(GIgBlocks.NEST_RESIN_WEB_CROSS)))
			return;

		var list = entity.level().getBlockStatesIfLoaded(entity.getBoundingBox().inflate(18.0, 18.0, 18.0));
		var randomPhase = entity.getRandom().nextInt(0, 100);
		if ((list.anyMatch(NEST) && randomPhase >= 50) && GigEntityUtils.isTargetHostable(target)) {
			entity.grabTarget(target);
		} else if ((target.getHealth() <= (target.getMaxHealth() * 0.50)) && randomPhase >= 80 && !target.getType().is(GigTags.XENO_EXECUTE_BLACKLIST)) {
			entity.grabTarget(target);
			entity.setIsBiting(true);
		} else {
			if (!entity.isVehicle()) {
				entity.doHurtTarget(target);
			}
		}
	}
}
