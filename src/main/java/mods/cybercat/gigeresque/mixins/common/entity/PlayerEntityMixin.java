package mods.cybercat.gigeresque.mixins.common.entity;

import mod.azure.azurelib.util.ClientUtils;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import net.minecraft.network.chat.Component;
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

import java.util.Objects;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = {"wantsToStopRiding"}, at = {@At("RETURN")}, cancellable = true)
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

    private long lastUpdateTime = 0L;

    private String generateTimerMessage() {
        var ticksAttached = ((FacehuggerEntity) Objects.requireNonNull(this.getFirstPassenger())).ticksAttachedToHost;
        var tickTimer = Gigeresque.config.facehuggerAttachTickTimer;

        // Calculate seconds if needed
        var secondsAttached = Gigeresque.config.enableFacehuggerTimerTicks ? ticksAttached : Math.round(
                ticksAttached / 20);
        var secondsTimer = Gigeresque.config.enableFacehuggerTimerTicks ? tickTimer : Math.round(tickTimer / 20);

        return "Attachment Timer: " + secondsAttached + " seconds / " + secondsTimer + " seconds";
    }

    @Inject(method = {"tick"}, at = {@At("HEAD")})
    public void tellPlayer(CallbackInfo ci) {
        if (this.level().isClientSide() && this.getPassengers().stream().anyMatch(
                FacehuggerEntity.class::isInstance) && Gigeresque.config.enableFacehuggerAttachmentTimer) {
            var player = ClientUtils.getClientPlayer();
            if (player != null) {
                // Get the current time in milliseconds
                var currentTime = System.currentTimeMillis();
                // Check if enough time has elapsed since the last update
                if (currentTime - lastUpdateTime >= 1000L) {
                    lastUpdateTime = currentTime; // Update last update time
                    var timerMessage = generateTimerMessage();
                    player.displayClientMessage(Component.literal(timerMessage), true);
                }
            }
        }
    }

}
