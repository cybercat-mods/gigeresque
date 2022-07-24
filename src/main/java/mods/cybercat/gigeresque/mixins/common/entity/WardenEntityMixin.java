package mods.cybercat.gigeresque.mixins.common.entity;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.WardenEntity;

@Mixin(WardenEntity.class)
public class WardenEntityMixin {

	@Inject(method = { "isValidTarget" }, at = { @At("HEAD") }, cancellable = true)
	void tick(@Nullable Entity entity, CallbackInfoReturnable<Boolean> ci) {
		if (entity instanceof AlienEntity)
			ci.setReturnValue(false);
	}
}
