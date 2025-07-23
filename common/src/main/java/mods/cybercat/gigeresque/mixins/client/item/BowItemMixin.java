package mods.cybercat.gigeresque.mixins.client.item;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import mods.cybercat.gigeresque.common.item.GigItems;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;

@Mixin(BowItem.class)
public class BowItemMixin {
    private static final Predicate<ItemStack> ALLOWED_PROJECTILES = stack -> {
        if (stack.is(GigItems.SEALED_AMPOULE_ACID.get()) || stack.is(GigItems.SEALED_AMPOULE_GOO.get())) {
            return false;
        }
        return stack.getItem() instanceof ArrowItem;
    };

    @Overwrite
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ALLOWED_PROJECTILES;
    }
}