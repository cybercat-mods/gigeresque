package mods.cybercat.gigeresque.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class AcidParticleFactory implements ParticleProvider<SimpleParticleType> {
	private final SpriteSet spriteProvider;

	public AcidParticleFactory(SpriteSet spriteProvider) {
		this.spriteProvider = spriteProvider;
	}

	@Override
	public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e,
			double f, double g, double h, double i) {
		return new AcidParticle(clientWorld, d, e, f, g, h, i, spriteProvider);
	}
}
