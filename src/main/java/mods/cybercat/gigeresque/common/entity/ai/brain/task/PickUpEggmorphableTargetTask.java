package mods.cybercat.gigeresque.common.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;

import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypes;
import mods.cybercat.gigeresque.common.entity.impl.AdultAlienEntity;
import mods.cybercat.gigeresque.common.util.EntityUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class PickUpEggmorphableTargetTask extends Task<AdultAlienEntity> {
	private final double speed;

	public PickUpEggmorphableTargetTask(double speed) {
		super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
				MemoryModuleTypes.EGGMORPH_TARGET, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.HOME,
				MemoryModuleState.VALUE_PRESENT));
		this.speed = speed;
	}

	@Override
	protected boolean shouldRun(ServerWorld world, AdultAlienEntity alien) {
		if (alien.getBrain().hasMemoryModule(MemoryModuleTypes.EGGMORPH_TARGET)) {
			var target = alien.getBrain().getOptionalMemory(MemoryModuleTypes.EGGMORPH_TARGET).orElse(null);
			if (target == null)
				return false;
			if (EntityUtils.isEggmorphable(target) && !EntityUtils.isFacehuggerAttached(target)) {
				return !alien.isCarryingEggmorphableTarget();
			} else if (target.world.getBlockState(target.getBlockPos()).isOf(GIgBlocks.NEST_RESIN_WEB_CROSS)) {
				return false;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	protected void run(ServerWorld serverWorld, AdultAlienEntity alien, long l) {
		var target = alien.getBrain().getOptionalMemory(MemoryModuleTypes.EGGMORPH_TARGET).orElse(null);
		if (target == null)
			return;

		if (target instanceof PlayerEntity)
			if (((PlayerEntity) target).isCreative() || ((PlayerEntity) target).isSpectator())
				return;

		if (alien.distanceTo(target) < 4.0) {
			if (target.getVehicle() == null) {
				target.startRiding(alien, true);
				((LivingEntity) target)
						.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1000, 10, false, false));
				target.setMovementSpeed(0.0f);
			}
		} else {
			alien.getNavigation().startMovingTo(target.getX(), target.getY(), target.getZ(), speed);
		}
	}
}
