package mods.cybercat.gigeresque.mixins.common.entity;

import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.NearestVisibleLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Thanks to Boston for this fix!
 */
@Mixin(VillagerHostilesSensor.class)
public abstract class VillagerHostilesSensorMixin extends NearestVisibleLivingEntitySensor {

    @Inject(at = @At("HEAD"), method = "isClose", cancellable = true)
    void isClose(LivingEntity livingEntity, LivingEntity livingEntity2, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (!livingEntity2.getType().is(GigTags.GIG_ALIENS)) return;

        var distance = 12F;
        var returnValue = livingEntity2.distanceToSqr(livingEntity) <= (distance * distance);
        callbackInfoReturnable.setReturnValue(returnValue);
    }

    @Inject(at = @At("HEAD"), method = "isHostile", cancellable = true)
    void isHostile(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (livingEntity.getType().is(GigTags.GIG_ALIENS)) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }
}
