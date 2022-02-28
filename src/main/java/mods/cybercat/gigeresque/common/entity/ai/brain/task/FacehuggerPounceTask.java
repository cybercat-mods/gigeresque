package mods.cybercat.gigeresque.common.entity.ai.brain.task;

import static mods.cybercat.gigeresque.common.util.MathUtil.clamp;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import mods.cybercat.gigeresque.common.entity.impl.FacehuggerEntity;
import com.google.common.collect.ImmutableMap;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class FacehuggerPounceTask extends Task<FacehuggerEntity> {
	public FacehuggerPounceTask() {
		super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
				MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT));
	}

	private static final double MAX_LEAP_DISTANCE = 8.0;

	@Override
	protected boolean shouldRun(ServerWorld serverWorld, FacehuggerEntity facehugger) {
		LivingEntity target = getTarget(facehugger);
		int yDiff = abs(facehugger.getBlockY() - target.getBlockY());
		return facehugger.isOnGround() && facehugger.distanceTo(target) < MAX_LEAP_DISTANCE && yDiff < 3
				&& facehugger.canSee(target);
	}

	@Override
	protected void run(ServerWorld serverWorld, FacehuggerEntity facehugger, long l) {
		var vec3d = facehugger.getVelocity();
		var target = getTarget(facehugger);
		var vec3d2 = new Vec3d(target.getX() - facehugger.getX(), 0.0, target.getZ() - facehugger.getZ());
		var length = sqrt(vec3d2.length());

		if (vec3d2.lengthSquared() > 1.0E-7) {
			vec3d2 = vec3d2.normalize().multiply(0.4).add(vec3d.multiply(0.2));
		}

		var maxXVel = clamp(vec3d2.x, -MAX_LEAP_DISTANCE, MAX_LEAP_DISTANCE);
		var maxZVel = clamp(vec3d2.z, -MAX_LEAP_DISTANCE, MAX_LEAP_DISTANCE);

		facehugger.addVelocity(maxXVel * length, target.getStandingEyeHeight() / 2.0, maxZVel * length);
	}

	private LivingEntity getTarget(PathAwareEntity entity) {
		return entity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
	}
}
