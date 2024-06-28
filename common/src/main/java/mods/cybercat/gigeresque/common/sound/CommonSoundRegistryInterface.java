package mods.cybercat.gigeresque.common.sound;

import mods.cybercat.gigeresque.platform.GigServices;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

/**
 * Example of using this Interface to create a new SoundEvent:
 * <p>
 * The following code demonstrates how to register a new sound event in the game:
 * </p>
 * <pre>{@code
 * public static Supplier<SoundEvent> TEST_SOUND = CommonSoundRegistryInterface.registerSound("modid", "test_sound", () -> SoundEvent.createVariableRangeEvent(
 *             ResourceLocation.fromNamespaceAndPath("modid", "test_sound")));
 * }</pre>
 * <p>
 * In this example:
 * </p>
 * <ul>
 * <li><code>registerSound</code> is a method to register a new sound event with the specified mod ID and sound name.</li>
 * <li><code>SoundEvent</code> is used to create a new sound event instance.</li>
 * </ul>
 * <p>
 * The {@link SoundEvent SoundEvent} class represents a sound event in the game.
 * </p>
 */
public interface CommonSoundRegistryInterface {

    /**
     * Registers a new sound event.
     *
     * @param modID     The mod ID.
     * @param soundName The name of the sound event.
     * @param sound     A supplier for the sound event.
     * @param <T>       The type of the sound event.
     * @return A supplier for the registered sound event.
     */
    static <T extends SoundEvent> Supplier<T> registerSound(String modID, String soundName, Supplier<T> sound) {
        return GigServices.COMMON_REGISTRY.registerSound(modID, soundName, sound);
    }
}