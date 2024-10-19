package mods.cybercat.gigeresque.common.entity.ai.tasks.movement;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;

public class LeapAtTargetTask<E extends StalkerEntity> extends DelayedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
        Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
        Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)
    );

    private static final double MAX_LEAP_DISTANCE = 3.0;

    @Nullable
    protected LivingEntity target = null;

    public LeapAtTargetTask(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        this.target = BrainUtils.getTargetOfEntity(entity);
        assert target != null;
        var yDiff = Mth.abs(entity.getBlockY() - target.getBlockY());
        return !entity.isVehicle() && this.target != null && entity.onGround() && entity.distanceTo(target) > MAX_LEAP_DISTANCE && yDiff > 3
            && entity.getBlockY() <= target.getBlockY();
    }

    @Override
    protected void doDelayedAction(E entity) {
        if (this.target == null)
            return;

        Vec3 vec3 = entity.getDeltaMovement();
        Vec3 vec32 = new Vec3(this.target.getX() - entity.getX(), 0.0, this.target.getZ() - entity.getZ());
        if (vec32.lengthSqr() > 1.0E-7)
            vec32 = vec32.normalize().scale(1.4).add(vec3.scale(0.2));
        entity.setDeltaMovement(vec32.x, 1.3F, vec32.z);
    }
}
