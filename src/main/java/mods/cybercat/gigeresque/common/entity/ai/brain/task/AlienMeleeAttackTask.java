package mods.cybercat.gigeresque.common.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;

import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

public class AlienMeleeAttackTask extends Task<AlienEntity> {
	private final int interval;

	public AlienMeleeAttackTask(int interval) {
		super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
				MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.ATTACK_COOLING_DOWN,
				MemoryModuleState.VALUE_ABSENT));
		this.interval = interval;
	}

	@Override
	protected boolean shouldRun(ServerWorld serverWorld, AlienEntity mobEntity) {
		LivingEntity livingEntity = this.getAttackTarget(mobEntity);
		return LookTargetUtil.isVisibleInMemory(mobEntity, livingEntity)
				&& LookTargetUtil.isTargetWithinAttackRange(mobEntity, livingEntity, 0)
				&& !(livingEntity.world.getBlockState(livingEntity.getBlockPos()).isOf(GIgBlocks.NEST_RESIN_WEB_CROSS));
	}

	@Override
	protected void run(ServerWorld serverWorld, AlienEntity mobEntity, long l) {
		LivingEntity livingEntity = this.getAttackTarget(mobEntity);
		LookTargetUtil.lookAt(mobEntity, livingEntity);
		if (!livingEntity.world.getBlockState(livingEntity.getBlockPos()).isOf(GIgBlocks.NEST_RESIN_WEB_CROSS)) {
			mobEntity.swingHand(Hand.MAIN_HAND);
			mobEntity.tryAttack(livingEntity);
			mobEntity.getBrain().remember(MemoryModuleType.ATTACK_COOLING_DOWN, true, this.interval);
		}
	}

	private LivingEntity getAttackTarget(MobEntity entity) {
		return entity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
	}
}
