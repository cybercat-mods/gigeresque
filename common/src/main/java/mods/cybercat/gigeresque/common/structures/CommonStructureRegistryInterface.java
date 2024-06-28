package mods.cybercat.gigeresque.common.structures;

import com.mojang.serialization.MapCodec;
import mods.cybercat.gigeresque.platform.GigServices;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.function.Supplier;

/**
 * Example of using this Interface to create a new Structure:
 * <p>
 * The following code demonstrates how to register a new structure in the game:
 * </p>
 * <pre>{@code
 * public static final Supplier<StructureType<CustomStructure>> TEST = CommonStructureRegistryInterface.registerStructure("modid", "structurename", CustomStructure.CODEC);
 * }</pre>
 * <p>
 * In this example:
 * </p>
 * <ul>
 * <li><code>registerStructure</code> is a method to register a new structure type with the specified mod ID and structure name.</li>
 * <li><code>"modid"</code> is the identifier for your mod.</li>
 * <li><code>"structurename"</code> is the name you want to give to your new structure.</li>
 * <li><code>CustomStructure.CODEC</code> is the codec for your custom structure type.</li>
 * </ul>
 */
public interface CommonStructureRegistryInterface {

    /**
     * Registers a new structure.
     *
     * @param modID         The mod ID.
     * @param structureName The name of the structure.
     * @param structure     A codec for the structure type.
     * @param <T>           The type of the structure.
     * @return A supplier for the registered structure type.
     */
    static <T extends Structure> Supplier<StructureType<T>> registerStructure(String modID, String structureName, MapCodec<T> structure) {
        return GigServices.COMMON_REGISTRY.registerStructure(modID, structureName, structure);
    }
}