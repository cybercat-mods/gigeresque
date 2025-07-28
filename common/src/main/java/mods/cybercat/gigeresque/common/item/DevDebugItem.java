package mods.cybercat.gigeresque.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
            if (!alienEntity.stasisManager.isStasis()) {
                alienEntity.stasisManager.setStasis(true);
                return InteractionResult.sidedSuccess(player.level().isClientSide);
            }
            if (alienEntity.stasisManager.isStasis()) {
                alienEntity.stasisManager.setStasis(false);
                return InteractionResult.sidedSuccess(player.level().isClientSide);
            }
        }
        return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }

    @Override
    public void appendHoverText(
        @NotNull ItemStack stack,
        Item.@NotNull TooltipContext context,
        @NotNull List<Component> tooltipComponents,
        @NotNull TooltipFlag tooltipFlag
    ) {
        tooltipComponents.add(
            Component.translatable("item.gigeresque.creativeonly.tooltip")
                .withStyle(ChatFormatting.DARK_RED)
                .withStyle(ChatFormatting.ITALIC)
        );
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
