package mods.cybercat.gigeresque.common.predicates;

import org.jetbrains.annotations.Nullable;

import mods.cybercat.gigeresque.common.item.GigItems;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;
//import net.minecraft.client.item.ModelPredicateProviderRegistry;

public class AmpoulePredicates {
    public static float acidLoadedPredicate(ItemStack stack, @Nullable Level world, @Nullable LivingEntity entity, int seed) {
        if (stack.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(stack)) {
            ChargedProjectiles projectiles = stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
            if (!projectiles.isEmpty() && projectiles.contains(GigItems.SEALED_AMPOULE_ACID.get())) {
                return 1.0F;
            }
        }
        return 0.0F;
    }

    public static float gooLoadedPredicate(ItemStack stack, @Nullable Level world, @Nullable LivingEntity entity, int seed) {
        if (stack.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(stack)) {
            ChargedProjectiles projectiles = stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
            if (!projectiles.isEmpty() && projectiles.contains(GigItems.SEALED_AMPOULE_GOO.get())) {
                return 1.0F;
            }
        }
        return 0.0F;
    }

        public static float acidLoadedPredicate(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int seed) {
        if (stack.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(stack)) {
            ChargedProjectiles projectiles = stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
            if (!projectiles.isEmpty() && projectiles.contains(GigItems.SEALED_AMPOULE_ACID.get())) {
                return 1.0F;
            }
        }
        return 0.0F;
    }

    public static float gooLoadedPredicate(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int seed) {
        if (stack.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(stack)) {
            ChargedProjectiles projectiles = stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
            if (!projectiles.isEmpty() && projectiles.contains(GigItems.SEALED_AMPOULE_GOO.get())) {
                return 1.0F;
            }
        }
        return 0.0F;
    }
}
