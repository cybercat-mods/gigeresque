package mods.cybercat.gigeresque.mixins.common.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import mods.cybercat.gigeresque.interfacing.IMobEntityLivingTickHook;
import mods.cybercat.gigeresque.interfacing.IMobEntityRegisterGoalsHook;
import mods.cybercat.gigeresque.interfacing.IMobEntityTickHook;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

/**
 * @author Boston Vanseghi
 */
@Mixin(Mob.class)
public abstract class MobEntityMixin extends LivingEntity implements IMobEntityLivingTickHook, IMobEntityTickHook, IMobEntityRegisterGoalsHook {
	
	@Inject(method = "aiStep", at = @At("HEAD"))
	private void onLivingTick(CallbackInfo ci) {
		this.onLivingTick();
	}

	@Override
	public void onLivingTick() { }

	@Inject(method = "tick()V", at = @At("RETURN"))
	private void onTick(CallbackInfo ci) {
		this.onTick();
	}

	@Override
	public void onTick() { }

	@Shadow(prefix = "shadow$")
	private void shadow$registerGoals() { }

	@Redirect(method = "<init>*", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Mob;registerGoals()V"
			))
	private void onRegisterGoals(Mob _this) {
		this.shadow$registerGoals();

		if(_this == (Object) this) {
			this.onRegisterGoals();
		}
	}

	@Override
	public void onRegisterGoals() { }

	@Shadow
	private boolean persistenceRequired;

	protected MobEntityMixin(EntityType<? extends LivingEntity> type, Level world) {
		super(type, world);
	}

	@Inject(method = { "playAmbientSound" }, at = { @At("HEAD") }, cancellable = true)
	public void playAmbientSound(CallbackInfo callbackInfo) {
		if (((Eggmorphable) this).isEggmorphing())
			callbackInfo.cancel();

		if (this.getPassengers().stream().anyMatch(FacehuggerEntity.class::isInstance))
			callbackInfo.cancel();
	}

	@Inject(method = { "requiresCustomPersistence" }, at = { @At("RETURN") }, cancellable = true)
	public boolean cannotDespawn(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (this instanceof Host host && host.hasParasite())
			callbackInfo.setReturnValue(true);
		if (((Eggmorphable) this).isEggmorphing())
			callbackInfo.setReturnValue(true);
		return callbackInfo.getReturnValue();
	}

	@Inject(method = { "tick" }, at = { @At("HEAD") })
	void tick(CallbackInfo callbackInfo) {
		if ((this instanceof Host host && host.hasParasite()) || this.hasEffect(GigStatusEffects.DNA))
			this.persistenceRequired = true;
	}
}
