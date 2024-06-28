package mods.cybercat.gigeresque.common.block;

import mods.cybercat.gigeresque.platform.GigServices;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

/**
 * Example of using this Interface to create a new Block:
 * <p>
 * The following code demonstrates how to register a new block in the game:
 * </p>
 * <pre>{@code
 * public static final Supplier<TestBlock> TEST_BLOCK = CommonBlockRegistryInterface.registerBlock("modid", "blockname", TestBlock::new);
 * }</pre>
 * <p>
 * In this example:
 * </p>
 * <ul>
 * <li><code>registerBlock</code> is a method to register a new block with the specified mod ID and block name.</li>
 * <li><code>TestBlock</code> is used to create a new block instance.</li>
 * </ul>
 * <p>
 * The {@link Block Block} class represents a block in the game.
 * </p>
 */
public interface CommonBlockRegistryInterface {

    /**
     * Registers a new Block.
     *
     * @param modID     The mod ID.
     * @param blockName The name of the block.
     * @param block     A supplier for the block.
     * @param <T>       The type of the block.
     * @return A supplier for the registered block.
     */
    static <T extends Block> Supplier<T> registerBlock(String modID, String blockName, Supplier<T> block) {
        return GigServices.COMMON_REGISTRY.registerBlock(modID, blockName, block);
    }
}