package mods.cybercat.gigeresque.common.item;

import mods.cybercat.gigeresque.platform.GigServices;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

/**
 * Example of using this Interface to create a new Item:
 * <p>
 * The following code demonstrates how to register a new item and a spawn egg in the game:
 * </p>
 * <pre>{@code
 * public static final Supplier<Item> TEST_ITEM = CommonItemRegistryInterface.registerItem("modid", "itemname", () -> new Item(new Item.Properties()));
 * public static final Supplier<SpawnEggItem> TEST_SPAWN_EGG = CommonItemRegistryInterface.registerItem("modid", "entityname_spawn_egg", Services.COMMON_REGISTRY.makeSpawnEggFor(TESTENTITY, 0x1F1F1F, 0x0D0D0D, new Item.Properties()));
 * }</pre>
 * <p>
 * In this example:
 * </p>
 * <ul>
 * <li><code>registerItem</code> is a method to register a new item with the specified mod ID and item name.</li>
 * <li><code>Item</code> is used to create a new item instance with default properties.</li>
 * <li><code>makeSpawnEggFor</code> is a method to create a spawn egg for the specified entity with primary and secondary colors.</li>
 * </ul>
 * <p>
 * The {@link Item Item} class represents an item in the game.
 * </p>
 * <p>
 * The {@link net.minecraft.world.item.SpawnEggItem SpawnEggItem} class represents a spawn egg in the game.
 * </p>
 */
public interface CommonItemRegistryInterface {

    /**
     * Registers a new Item.
     *
     * @param modID    The mod ID.
     * @param itemName The name of the item.
     * @param item     A supplier for the item.
     * @param <T>      The type of the item.
     * @return A supplier for the registered item.
     */
    static <T extends Item> Supplier<T> registerItem(String modID, String itemName, Supplier<T> item) {
        return GigServices.COMMON_REGISTRY.registerItem(modID, itemName, item);
    }
}