package mods.cybercat.gigeresque.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class GooParticleFactory implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet spriteProvider;

    public GooParticleFactory(SpriteSet spriteProvider) {
        this.spriteProvider = spriteProvider;
    }

    @Override
    public Particle createParticle(@NotNull SimpleParticleType defaultParticleType, @NotNull ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
        return new GooParticle(clientWorld, d, e, f, g, h, i, spriteProvider);
    }
}
