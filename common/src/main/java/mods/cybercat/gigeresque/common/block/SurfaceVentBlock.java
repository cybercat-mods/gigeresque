package mods.cybercat.gigeresque.common.block;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * TODO: <a href="https://trello.com/c/yFqxRPYe/33-alien-vents">Trello Board</a>
 */
public class SurfaceVentBlock extends Block {

    public SurfaceVentBlock() {
        super(Properties.of().explosionResistance(Float.MAX_VALUE).strength(Float.MAX_VALUE, Float.MAX_VALUE).noOcclusion().noLootTable());
    }

    @Override
    public void appendHoverText(
        @NotNull ItemStack stack,
        Item.@NotNull TooltipContext context,
        @NotNull List<Component> tooltipComponents,
        @NotNull TooltipFlag tooltipFlag
    ) {
        tooltipComponents.add(
            Component.translatable("block.gigeresque.unfinished.tooltip")
                .withStyle(ChatFormatting.DARK_RED)
                .withStyle(ChatFormatting.ITALIC)
        );
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
