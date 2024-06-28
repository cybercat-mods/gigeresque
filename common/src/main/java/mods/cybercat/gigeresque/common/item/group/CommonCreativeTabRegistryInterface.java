package mods.cybercat.gigeresque.common.item.group;

/**
 * Example of using this Interface to create a new Creative Tab:
 * <p>
 * The following code demonstrates how to register a new creative mode tab in the game:
 * </p>
 * <pre>{@code
 * public static final Supplier<CreativeModeTab> EXAMPLEMOD_TAB = Services.COMMON_REGISTRY.registerCreativeModeTab(
 *         AzureLib.MOD_ID,
 *         "examplemod_items",
 *         () -> Services.COMMON_REGISTRY.newCreativeTabBuilder()
 *             .title(Component.translatable("itemGroup." + AzureLib.MOD_ID + ".examplemod_items"))
 *             .icon(() -> new ItemStack(Items.ITEM_FRAME))
 *             .displayItems((enabledFeatures, entries) -> entries.accept(Items.ITEM_FRAME))
 *             .build());
 * }</pre>
 * <p>
 * In this example:
 * </p>
 * <ul>
 * <li><code>registerCreativeModeTab</code> is a method to register a new creative mode tab with the specified mod ID and tab name.</li>
 * <li>The <code>newCreativeTabBuilder</code> method is used to build the creative tab with a title, icon, and displayed items.</li>
 * </ul>
 * <p>
 * The {@link net.minecraft.world.item.CreativeModeTab CreativeModeTab} class represents a tab in the creative inventory menu.
 * </p>
 */
public interface CommonCreativeTabRegistryInterface {
}
