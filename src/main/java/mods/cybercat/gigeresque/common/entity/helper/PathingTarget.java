package mods.cybercat.gigeresque.common.entity.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public record PathingTarget(BlockPos pos, Direction side) {
}