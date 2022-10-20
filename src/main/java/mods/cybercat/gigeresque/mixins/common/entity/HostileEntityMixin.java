package mods.cybercat.gigeresque.mixins.common.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

@Mixin(Monster.class)
public abstract class HostileEntityMixin extends LivingEntity {

	protected HostileEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = { "isDarkEnoughToSpawn" }, at = { @At("RETURN") })
	private static boolean cannotDescanSpawnInDarkpawn(ServerLevelAccessor world, BlockPos pos, RandomSource random,
			CallbackInfoReturnable<Boolean> callbackInfo) {
		if (world.getBlockState(pos.below()).is(GigTags.DUNGEON_BLOCKS)) {
			return false;
		}
		return callbackInfo.getReturnValue();
	}

	@Inject(method = { "canSpawnInDark" }, at = { @At("RETURN") })
	private static boolean cannotDespawn(EntityType<? extends Monster> type, ServerLevelAccessor world,
			MobSpawnType spawnReason, BlockPos pos, RandomSource random, CallbackInfoReturnable<Boolean> callbackInfo) {
		return world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random)
				&& Monster.checkMobSpawnRules(type, world, spawnReason, pos, random)
				&& !world.getBlockState(pos.below()).is(GigTags.DUNGEON_BLOCKS);
	}
}
