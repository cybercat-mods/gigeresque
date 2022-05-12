package mods.cybercat.gigeresque.common.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;

import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

public class RunnerBusterMeleeAttackTask extends Task<MobEntity> {
	private final int interval;

	public RunnerBusterMeleeAttackTask(int interval) {
		super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
				MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.ATTACK_COOLING_DOWN,
				MemoryModuleState.VALUE_ABSENT));
		this.interval = interval;
	}

	@Override
	protected boolean shouldRun(ServerWorld serverWorld, MobEntity mobEntity) {
		LivingEntity livingEntity = this.getAttackTarget(mobEntity);
		return LookTargetUtil.isVisibleInMemory(mobEntity, livingEntity)
				&& LookTargetUtil.isTargetWithinMeleeRange(mobEntity, livingEntity)
				&& !((Host) livingEntity).hasParasite();
	}

	@Override
	protected void run(ServerWorld serverWorld, MobEntity mobEntity, long l) {
		LivingEntity livingEntity = this.getAttackTarget(mobEntity);
		LookTargetUtil.lookAt(mobEntity, livingEntity);
		mobEntity.swingHand(Hand.MAIN_HAND);
		mobEntity.tryAttack(livingEntity);
		mobEntity.getBrain().remember(MemoryModuleType.ATTACK_COOLING_DOWN, true, this.interval);
	}

	private LivingEntity getAttackTarget(MobEntity entity) {
		return entity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
	}
}