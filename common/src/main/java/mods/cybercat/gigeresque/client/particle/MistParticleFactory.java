package mods.cybercat.gigeresque.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

/**
 * This class is licensed under the MIT License. See the full license here:
 * <a href="https://github.com/PigCart/particle-rain/blob/main/LICENSE">MIT License</a>. <br>
 * This is a modified version of the original class: <a href=
 * "https://github.com/PigCart/particle-rain/blob/1.21/src/main/java/pigcart/particlerain/particle/GroundFogParticle.java">
 * GroundFogParticle.java</a>. <br>
 * Credit to PigCart and the Particle Rain project for the original implementation.
 */
public class MistParticleFactory implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet spriteProvider;

    public MistParticleFactory(SpriteSet spriteProvider) {
        this.spriteProvider = spriteProvider;
    }

    @Override
    public Particle createParticle(
        SimpleParticleType parameters,
        ClientLevel level,
        double x,
        double y,
        double z,
        double velocityX,
        double velocityY,
        double velocityZ
    ) {
        return new MistParticle(level, x, y, z, this.spriteProvider);
    }
}
