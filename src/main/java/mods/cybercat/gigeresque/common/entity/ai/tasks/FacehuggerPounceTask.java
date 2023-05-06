package mods.cybercat.gigeresque.common.entity.ai.tasks;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

public class FacehuggerPounceTask<E extends Mob> extends DelayedBehaviour<E> {
	private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
			Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
			Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
	private static final double MAX_LEAP_DISTANCE = 8.0;

	@Nullable
	protected LivingEntity target = null;

	public FacehuggerPounceTask(int delayTicks) {
		super(delayTicks);
	}

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
		this.target = BrainUtils.getTargetOfEntity(entity);
		var yDiff = Mth.abs(entity.getBlockY() - target.getBlockY());
		return target != null && entity.isOnGround() && entity.distanceTo(target) < MAX_LEAP_DISTANCE && yDiff < 3
				&& entity.hasLineOfSight(target)
				&& (target.getVehicle() != null
						&& !target.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance))
				&& !GigEntityUtils.isFacehuggerAttached(target) && !entity.isInWater();
	}

	@Override
	protected void doDelayedAction(E entity) {
		if (this.target == null)
			return;
		var vec3d = entity.getDeltaMovement();
		this.target = BrainUtils.getTargetOfEntity(entity);
		var vec3d2 = new Vec3(target.getX() - entity.getX(), 0.0, target.getZ() - entity.getZ());
		var length = Mth.sqrt((float) vec3d2.length());

		if (vec3d2.lengthSqr() > 1.0E-7)
			vec3d2 = vec3d2.normalize().scale(0.2).add(vec3d.scale(0.2));

		var maxXVel = Mth.clamp(vec3d2.x, -MAX_LEAP_DISTANCE, MAX_LEAP_DISTANCE);
		var maxZVel = Mth.clamp(vec3d2.z, -MAX_LEAP_DISTANCE, MAX_LEAP_DISTANCE);

		if (!entity.horizontalCollision)
			entity.push(maxXVel * length, target.getEyeHeight() / 2.5, maxZVel * length);
	}
}
