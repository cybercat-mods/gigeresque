package mods.cybercat.gigeresque.common.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class DevDebugItem extends Item {

    public DevDebugItem() {
        super(new Properties());
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(
        @NotNull ItemStack stack,
        @NotNull Player player,
        @NotNull LivingEntity interactionTarget,
        @NotNull InteractionHand usedHand
    ) {
        if (interactionTarget instanceof AlienEntity alienEntity) {
            if (!alienEntity.isPassedOut()) {
                alienEntity.setPassedOutStatus(true);
                return InteractionResult.sidedSuccess(player.level().isClientSide);
            }
            if (alienEntity.isPassedOut()) {
                alienEntity.setPassedOutStatus(false);
                return InteractionResult.sidedSuccess(player.level().isClientSide);
            }
        }
        return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }
}
