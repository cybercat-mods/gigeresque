package mods.cybercat.gigeresque.mixins.common.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.FacehuggerEntity;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements Eggmorphable {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = { "shouldDismount" }, at = { @At("RETURN") })
	protected boolean shouldDismount(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (this.getVehicle() instanceof AlienEntity) {
			return false;
		}
		return callbackInfo.getReturnValue();
	}

	@Inject(method = { "interact" }, at = { @At("HEAD") }, cancellable = true)
	protected ActionResult stopPlayerUsing(Entity entity, Hand hand,
			CallbackInfoReturnable<ActionResult> callbackInfo) {
		if (this.getPassengerList().stream().anyMatch(FacehuggerEntity.class::isInstance)) {
			callbackInfo.setReturnValue(ActionResult.FAIL);
		}
		return callbackInfo.getReturnValue();
	}

	@Inject(method = { "attack" }, at = { @At("HEAD") }, cancellable = true)
	protected void noAttacking(Entity target, CallbackInfo callbackInfo) {
		if (this.getPassengerList().stream().anyMatch(FacehuggerEntity.class::isInstance)) {
            this.clearActiveItem();
		}
	}

	@Inject(method = { "tickMovement" }, at = { @At("HEAD") }, cancellable = true)
	public void tickMovement(CallbackInfo callbackInfo) {
		if (this.getPassengerList().stream().anyMatch(FacehuggerEntity.class::isInstance)) {
			callbackInfo.cancel();
		}
	}

}
