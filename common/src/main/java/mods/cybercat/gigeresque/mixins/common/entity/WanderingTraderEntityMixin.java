package mods.cybercat.gigeresque.mixins.common.entity;

import mods.cybercat.gigeresque.common.item.SurgeryKitItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Boston Vanseghi
 */
@Mixin(WanderingTrader.class)
public abstract class WanderingTraderEntityMixin {

    @Inject(method = {"mobInteract"}, at = {@At("HEAD")}, cancellable = true)
    protected void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> callbackInfo) {
        if (player.getItemInHand(hand).getItem() instanceof SurgeryKitItem)
            callbackInfo.setReturnValue(callbackInfo.getReturnValue());
    }
}
