package mods.cybercat.gigeresque.mixins.common.entity;

import mods.cybercat.gigeresque.common.item.SurgeryKitItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Horse.class)
public abstract class RealHorseMixin extends AbstractHorse {

    protected RealHorseMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = { "mobInteract" }, at = { @At("HEAD") }, cancellable = true)
    protected void gigeresque$mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> callbackInfo) {
        if (player.getItemInHand(hand).getItem() instanceof SurgeryKitItem) {
            player.getItemInHand(hand).interactLivingEntity(player, this, hand);
        }
    }
}
