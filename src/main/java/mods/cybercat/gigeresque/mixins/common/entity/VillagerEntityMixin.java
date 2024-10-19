package mods.cybercat.gigeresque.mixins.common.entity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mods.cybercat.gigeresque.common.item.SurgeryKitItem;

/**
 * @author Boston Vanseghi
 */
@Mixin(Villager.class)
public abstract class VillagerEntityMixin {

    @Inject(method = { "mobInteract" }, at = { @At("HEAD") }, cancellable = true)
    protected void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> callbackInfo) {
        if (player.getItemInHand(hand).getItem() instanceof SurgeryKitItem)
            callbackInfo.setReturnValue(callbackInfo.getReturnValue());
    }
}
