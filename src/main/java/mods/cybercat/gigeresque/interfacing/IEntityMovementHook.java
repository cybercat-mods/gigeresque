package mods.cybercat.gigeresque.interfacing;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface IEntityMovementHook {
	public boolean onMove(MoverType type, Vec3 pos, boolean pre);

	@Nullable
	public BlockPos getAdjustedOnPosition(BlockPos onPosition);

	public boolean getAdjustedCanTriggerWalking(boolean canTriggerWalking);
}
