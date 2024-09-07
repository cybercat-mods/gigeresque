package mods.cybercat.gigeresque.mixins.common.crawling;

import mods.azure.bettercrawling.entity.mob.ILivingEntityDataManagerHook;
import mods.azure.bettercrawling.entity.mob.ILivingEntityJumpHook;
import mods.azure.bettercrawling.entity.mob.ILivingEntityLookAtHook;
import mods.azure.bettercrawling.entity.mob.ILivingEntityTravelHook;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ILivingEntityLookAtHook, ILivingEntityDataManagerHook, ILivingEntityTravelHook, ILivingEntityJumpHook {
	@ModifyVariable(method = "lookAt", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private Vec3 onLookAtModify(Vec3 vec, Anchor anchor, Vec3 vec2) {
		return this.onLookAt(anchor, vec);
	}

	@Override
	public Vec3 onLookAt(Anchor anchor, Vec3 vec) {
		return vec;
	}

	@Inject(method = "onSyncedDataUpdated", at = @At("HEAD"))
	private void onNotifyDataManagerChange(EntityDataAccessor<?> key, CallbackInfo ci) {
		this.onNotifyDataManagerChange(key);
	}

	@Override
	public void onNotifyDataManagerChange(EntityDataAccessor<?> key) { }

	@Inject(method = "travel", at = @At("HEAD"), cancellable = true)
	private void onTravelPre(Vec3 relative, CallbackInfo ci) {
		if(this.onTravel(relative, true)) {
			ci.cancel();
		}
	}

	@Inject(method = "travel", at = @At("RETURN"))
	private void onTravelPost(Vec3 relative, CallbackInfo ci) {
		this.onTravel(relative, false);
	}

	@Override
	public boolean onTravel(Vec3 relative, boolean pre) {
		return false;
	}

	@Inject(method = "jumpFromGround()V", at = @At("HEAD"), cancellable = true)
	private void onJump(CallbackInfo ci) {
		if(this.onJump()) {
			ci.cancel();
		}
	}

	@Override
	public boolean onJump() {
		return false;
	}
}
