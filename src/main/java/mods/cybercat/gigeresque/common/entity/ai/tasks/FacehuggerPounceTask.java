package mods.cybercat.gigeresque.common.entity.ai.tasks;

import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.Items;
import net.tslat.smartbrainlib.util.BrainUtils;

public class FacehuggerPounceTask<E extends FacehuggerEntity> extends CustomDelayedMeleeBehaviour<E> {
	private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT));
	protected Function<E, Integer> attackIntervalSupplier = entity -> 20;

	@Nullable
	protected LivingEntity target = null;

	public FacehuggerPounceTask(int delayTicks) {
		super(delayTicks);
	}

	/**
	 * Set the time between attacks.
	 * 
	 * @param supplier The tick value provider
	 * @return this
	 */
	public FacehuggerPounceTask<E> attackInterval(Function<E, Integer> supplier) {
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
		var huggerchecklist = !((this.target instanceof AlienEntity || this.target instanceof Warden || this.target instanceof ArmorStand) || (this.target.getVehicle() != null && this.target.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance)) || (this.target instanceof AlienEggEntity) || ((Host) this.target).isBleeding() || this.target.getMobType() == MobType.UNDEAD || ((Eggmorphable) this.target).isEggmorphing() || (GigEntityUtils.isFacehuggerAttached(this.target))
				|| (this.target.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS)) && GigEntityUtils.isTargetHostable(this.target) && this.target.isAlive();
		return huggerchecklist && entity.isWithinMeleeAttackRange(this.target);
	}

	@Override
	protected void start(E entity) {
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

		if (!entity.isWithinMeleeAttackRange(this.target))
			return;

		if (!this.target.getUseItem().is(Items.SHIELD))
			entity.grabTarget(this.target);
	}
}
