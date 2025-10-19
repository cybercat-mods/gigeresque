package mods.cybercat.gigeresque.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import mods.cybercat.gigeresque.common.entity.impl.projectile.GooAmpouleProjectile;

public class GooAmpouleItem extends ArrowItem {

    public GooAmpouleItem(Properties properties) {
        super(properties);
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

    @Override
    public @NotNull AbstractArrow createArrow(
        @NotNull Level level,
        ItemStack ammo,
        @NotNull LivingEntity shooter,
        @Nullable ItemStack weapon
    ) {
        return new GooAmpouleProjectile(level, shooter, ammo.copyWithCount(1), weapon);
    }

    @Override
    public @NotNull Projectile asProjectile(
        @NotNull Level level,
        @NotNull Position pos,
        @NotNull ItemStack stack,
        @NotNull Direction direction
    ) {
        return null;
    }
}
