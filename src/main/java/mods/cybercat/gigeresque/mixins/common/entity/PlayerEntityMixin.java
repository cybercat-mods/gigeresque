package mods.cybercat.gigeresque.mixins.common.entity;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements Eggmorphable {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = {"wantsToStopRiding"}, at = {@At("RETURN")})
    protected void shouldDismount(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this.getVehicle() instanceof AlienEntity) callbackInfo.setReturnValue(false);
    }

    @Inject(method = {"interactOn"}, at = {@At("HEAD")}, cancellable = true)
    protected void stopPlayerUsing(Entity entity, InteractionHand hand, CallbackInfoReturnable<InteractionResult> callbackInfo) {
        if (this.getPassengers().stream().anyMatch(FacehuggerEntity.class::isInstance))
            callbackInfo.setReturnValue(InteractionResult.FAIL);
    }

    @Inject(method = {"attack"}, at = {@At("HEAD")}, cancellable = true)
    protected void noAttacking(Entity target, CallbackInfo callbackInfo) {
        if (this.getPassengers().stream().anyMatch(FacehuggerEntity.class::isInstance)) this.stopUsingItem();
    }

    @Inject(method = {"aiStep"}, at = {@At("HEAD")}, cancellable = true)
    public void tickMovement(CallbackInfo callbackInfo) {
        if (this.getPassengers().stream().anyMatch(FacehuggerEntity.class::isInstance)) callbackInfo.cancel();
    }

}
