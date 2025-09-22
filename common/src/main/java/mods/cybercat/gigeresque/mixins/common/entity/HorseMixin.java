package mods.cybercat.gigeresque.mixins.common.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.cybercat.gigeresque.common.item.SurgeryKitItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Horse.class)
public abstract class HorseMixin extends AbstractHorse {

    protected HorseMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

        private Player gigeresque$cachedPlayer;

        private InteractionHand gigeresque$cachedHand;

        @Inject(
                method = "mobInteract",
                at = @At("HEAD")
        )
        private void cachePlayerAndHand(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
            this.gigeresque$cachedPlayer = player;
            this.gigeresque$cachedHand = hand;
        }

        @WrapOperation(
                method = "mobInteract",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/world/entity/animal/horse/Horse;makeMad()V"
                )
        )
        private void wrapMakeMad(Horse instance, Operation<Void> original) {
            if (gigeresque$cachedPlayer != null && gigeresque$cachedHand != null) {
                ItemStack itemInHand = gigeresque$cachedPlayer.getItemInHand(gigeresque$cachedHand);

                if (!itemInHand.isEmpty() && itemInHand.getItem() instanceof SurgeryKitItem) {
                    itemInHand.interactLivingEntity(gigeresque$cachedPlayer, instance, gigeresque$cachedHand);
                }
            }

            original.call(instance);
        }

}

