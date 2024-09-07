package mods.azure.bettercrawling.entity.mob;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public record PathingTarget(BlockPos pos, Direction side) {
}