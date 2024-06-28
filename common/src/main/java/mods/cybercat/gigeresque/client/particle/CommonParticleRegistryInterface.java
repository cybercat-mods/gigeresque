package mods.cybercat.gigeresque.client.particle;

import mods.cybercat.gigeresque.platform.GigServices;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Supplier;

/**
 * Example of using this Interface to create a new Particle:
 * <p>
 * The following code demonstrates how to register a new particle type in the game:
 * </p>
 * <pre>{@code
 * public static final Supplier<SimpleParticleType> TEST = CommonParticleRegistryInterface.registerParticle("modid", "particlename", () -> new SimpleParticleType(true));
 * }</pre>
 * <p>
 * In this example:
 * </p>
 * <ul>
 * <li><code>registerParticle</code> is a method to register a new particle with the specified mod ID and particle name.</li>
 * <li><code>SimpleParticleType</code> is used to create a new particle type instance.</li>
 * </ul>
 * <p>
 * The {@link net.minecraft.core.particles.SimpleParticleType SimpleParticleType} class represents a basic particle type in the game.
 * </p>
 */
public interface CommonParticleRegistryInterface {

    /**
     * Registers a new Particle.
     *
     * @param modID        The mod ID.
     * @param particleName The ID of the particle.
     * @param particle     A supplier for the particle type.
     * @param <T>          The type of the particle type.
     * @return A supplier for the registered particle type.
     */
    static <T extends ParticleType<?>> Supplier<T> registerParticle(String modID, String particleName, Supplier<T> particle) {
        return GigServices.COMMON_REGISTRY.registerParticle(modID, particleName, particle);
    }
}