package mods.cybercat.gigeresque.client.particle;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class Particles implements GigeresqueInitializer {
	public static final SimpleParticleType ACID = FabricParticleTypes.simple();
	public static final SimpleParticleType GOO = FabricParticleTypes.simple();
	public static final SimpleParticleType BLOOD = FabricParticleTypes.simple();

	@Override
	public void initialize() {
		registerParticle("acid", ACID, AcidParticleFactory::new);
		registerParticle("goo", GOO, GooParticleFactory::new);
		registerParticle("blood", BLOOD, BloodParticleFactory::new);
	}

	private void registerParticle(String path, SimpleParticleType type,
			ParticleFactoryRegistry.PendingParticleFactory<SimpleParticleType> factory) {
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, new ResourceLocation(Gigeresque.MOD_ID, path), type);
		ParticleFactoryRegistry.getInstance().register(type, factory);
	}
}
