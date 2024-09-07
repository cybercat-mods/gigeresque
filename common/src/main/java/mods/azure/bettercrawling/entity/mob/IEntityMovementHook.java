package mods.azure.bettercrawling.entity.mob;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface IEntityMovementHook {
	boolean onMove(MoverType type, Vec3 pos, boolean pre);

	@Nullable
    BlockPos getAdjustedOnPosition(BlockPos onPosition);

	boolean getAdjustedCanTriggerWalking(boolean canTriggerWalking);
}
