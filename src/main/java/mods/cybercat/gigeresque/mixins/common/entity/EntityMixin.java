package mods.cybercat.gigeresque.mixins.common.entity;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Thanks to Boston for this fix!
 */
@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(at = @At("HEAD"), method = "startRiding", cancellable = true)
    void boatRidingCancel(Entity entity, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        var self = Constants.<Entity>self(this);

        if (!self.getType().is(GigTags.GIG_ALIENS)) return;

        if (entity instanceof Boat || entity instanceof Minecart) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(at = @At("HEAD"), method = "tick")
    void kickOut(CallbackInfo callbackInfo) {
        var self = Constants.<Entity>self(this);
        var level = self.level();

        if (level.isClientSide) return;
        if (!self.getType().is(GigTags.GIG_ALIENS)) return;

        if (self.getVehicle() instanceof Boat || self.getVehicle() instanceof Minecart) {
            self.stopRiding();
        }
    }
}
