package mods.cybercat.gigeresque.mixins.common.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

@Mixin(HostileEntity.class)
public abstract class HostileEntityMixin extends LivingEntity {

	protected HostileEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = { "isSpawnDark" }, at = { @At("RETURN") })
	private static boolean cannotDescanSpawnInDarkpawn(ServerWorldAccess world, BlockPos pos, Random random,
			CallbackInfoReturnable<Boolean> callbackInfo) {
		if (world.getBlockState(pos.down()).isIn(GigTags.DUNGEON_BLOCKS)) {
			return false;
		}
		return callbackInfo.getReturnValue();
	}

	@Inject(method = { "canSpawnInDark" }, at = { @At("RETURN") })
	private static boolean cannotDespawn(EntityType<? extends HostileEntity> type, ServerWorldAccess world,
			SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> callbackInfo) {
		return world.getDifficulty() != Difficulty.PEACEFUL && HostileEntity.isSpawnDark(world, pos, random)
				&& HostileEntity.canMobSpawn(type, world, spawnReason, pos, random)
				&& !world.getBlockState(pos.down()).isIn(GigTags.DUNGEON_BLOCKS);
	}
}
