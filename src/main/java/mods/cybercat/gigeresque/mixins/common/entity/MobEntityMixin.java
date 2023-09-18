package mods.cybercat.gigeresque.mixins.common.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

/**
 * @author Boston Vanseghi
 */
@Mixin(Mob.class)
public abstract class MobEntityMixin extends LivingEntity {

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
