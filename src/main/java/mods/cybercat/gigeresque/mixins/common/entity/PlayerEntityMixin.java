package mods.cybercat.gigeresque.mixins.common.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.FacehuggerEntity;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements Eggmorphable {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = { "wantsToStopRiding" }, at = { @At("RETURN") })
	protected boolean shouldDismount(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (this.getVehicle() instanceof AlienEntity)
			return false;
		return callbackInfo.getReturnValue();
	}

	@Inject(method = { "interactOn" }, at = { @At("HEAD") }, cancellable = true)
	protected InteractionResult stopPlayerUsing(Entity entity, InteractionHand hand,
			CallbackInfoReturnable<InteractionResult> callbackInfo) {
		if (this.getPassengers().stream().anyMatch(FacehuggerEntity.class::isInstance))
			callbackInfo.setReturnValue(InteractionResult.FAIL);
		return callbackInfo.getReturnValue();
	}

	@Inject(method = { "attack" }, at = { @At("HEAD") }, cancellable = true)
	protected void noAttacking(Entity target, CallbackInfo callbackInfo) {
		if (this.getPassengers().stream().anyMatch(FacehuggerEntity.class::isInstance))
			this.stopUsingItem();
	}

	@Inject(method = { "aiStep" }, at = { @At("HEAD") }, cancellable = true)
	public void tickMovement(CallbackInfo callbackInfo) {
		if (this.getPassengers().stream().anyMatch(FacehuggerEntity.class::isInstance))
			callbackInfo.cancel();
	}

}
