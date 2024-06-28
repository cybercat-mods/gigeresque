package mods.cybercat.gigeresque.common.fluid;

import mods.cybercat.gigeresque.platform.GigServices;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

/**
 * Example of using this Interface to create a new Fluid:
 * <p>
 * The following code demonstrates how to register a new Fluid in the game:
 * </p>
 * <pre>{@code
 * public static final Supplier<Fluid> TEST_FLUID = CommonFluidRegistryInterface.registerFluid("modid", "fluidName", () -> new CustomFluid());
 * }</pre>
 * <p>
 * In this example:
 * </p>
 * <ul>
 * <li><code>registerFluid</code> is a method to register a new fluid with the specified mod ID and fluid name.</li>
 * <li><code>Fluid</code> is the base class for all fluids in the game.</li>
 * <li><code>CustomFluid</code> is a user-defined class extending <code>Fluid</code>.</li>
 * </ul>
 * <p>
 * The {@link Fluid Fluid} class represents a fluid in the game.
 * </p>
 */
public interface CommonFluidRegistryInterface {

    /**
     * Registers a new fluid.
     *
     * @param modID    The mod ID.
     * @param itemName The name of the fluid.
     * @param fluid    A supplier that provides an instance of the fluid.
     * @param <T>      The type of the fluid extending from {@link Fluid}.
     * @return A supplier that provides the registered fluid.
     */
    static <T extends Fluid> Supplier<T> registerFluid(String modID, String itemName, Supplier<T> fluid) {
        return GigServices.COMMON_REGISTRY.registerFluid(modID, itemName, fluid);
    }
}
