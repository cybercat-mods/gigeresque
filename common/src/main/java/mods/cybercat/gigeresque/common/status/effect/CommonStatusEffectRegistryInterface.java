package mods.cybercat.gigeresque.common.status.effect;

import mods.cybercat.gigeresque.platform.GigServices;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

import java.util.function.Supplier;

/**
 * Example of using this Interface to create a new Status Effect:
 * <p>
 * The following code demonstrates how to register a new Status Effect in the game:
 * </p>
 * <pre>{@code
 * public static final Holder<MobEffect> TEST_EFFECT = CommonStatusEffectRegistryInterface.registerStatusEffect("modid", "effectName", () -> new CustomMobEffect(
 *             MobEffectCategory.HARMFUL, Color.BLACK.getColor()));
 * }</pre>
 * <p>
 * In this example:
 * </p>
 * <ul>
 * <li><code>registerStatusEffect</code> is a method to register a new status effect with the specified mod ID and effect name.</li>
 * <li><code>MobEffect</code> is the base class for all status effects in the game.</li>
 * <li><code>CustomMobEffect</code> is a user-defined class extending <code>MobEffect</code>.</li>
 * <li><code>MobEffectCategory.HARMFUL</code> specifies the category of the status effect.</li>
 * <li><code>Color.BLACK.getColor()</code> specifies the color of the status effect.</li>
 * </ul>
 * <p>
 * The {@link MobEffect MobEffect} class represents a status effect in the game.
 * </p>
 * <p>
 * The {@link net.minecraft.world.effect.MobEffectCategory MobEffectCategory} class represents the category of a status effect in the game.
 * </p>
 */
public interface CommonStatusEffectRegistryInterface {

    /**
     * Registers a new Status Effect.
     *
     * @param modID        The mod ID.
     * @param effectName   The name of the Status Effect.
     * @param statusEffect A supplier that provides an instance of the Status Effect.
     * @param <T>          The type of the status effect extending from {@link MobEffect}.
     * @return A holder that provides the registered status effect.
     */
    static <T extends MobEffect> Holder<T> registerStatusEffect(String modID, String effectName, Supplier<T> statusEffect) {
        return GigServices.COMMON_REGISTRY.registerStatusEffect(modID, effectName, statusEffect);
    }
}
