package mods.cybercat.gigeresque;

import java.util.Optional;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.Vec3;

public record Constants() {

	public static final int TPS = 20; // Ticks per second
	public static final int TPM = TPS * 60; // Ticks per minute
	public static final int TPD = TPM * 20; // Ticks per day

	public static final ResourceLocation modResource(String name) {
		return new ResourceLocation(Gigeresque.MOD_ID, name);
	}

	/*
	 * Credit to: https://github.com/Nyfaria/NyfsSpiders/tree/1.20.x
	 */
	public static BlockPos blockPos(double pX, double pY, double pZ) {
		return new BlockPos(Mth.floor(pX), Mth.floor(pY), Mth.floor(pZ));
	}

	public static BlockPos blockPos(Vec3 pVec3) {
		return blockPos(pVec3.x, pVec3.y, pVec3.z);
	}

	public static Optional<EntityDimensions> onEntitySize(Entity entity) {
		if (entity instanceof ClassicAlienEntity)
			return Optional.of(EntityDimensions.scalable(0.9f, 2.45f));
		return Optional.empty();
	}
}
