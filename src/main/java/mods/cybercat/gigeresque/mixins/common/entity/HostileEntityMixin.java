package mods.cybercat.gigeresque.mixins.common.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
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
	private static boolean isDarkEnoughToSpawn(ServerLevelAccessor world, BlockPos pos, RandomSource random,
			CallbackInfoReturnable<Boolean> callbackInfo) {
		if (world.getBlockState(pos.below()).is(GigTags.DUNGEON_BLOCKS))
			return false;
		return callbackInfo.getReturnValue();
	}

	@Inject(method = { "checkMonsterSpawnRules" }, at = { @At("RETURN") })
	private static boolean checkMonsterSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor world,
			MobSpawnType spawnReason, BlockPos pos, RandomSource random, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (world.getBlockState(pos).is(GigTags.DUNGEON_BLOCKS))
			return false;
		return callbackInfo.getReturnValue();
	}
}
