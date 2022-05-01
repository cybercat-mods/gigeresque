package mods.cybercat.gigeresque.client.particle;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Particles implements GigeresqueInitializer {
	public static final DefaultParticleType ACID = FabricParticleTypes.simple();
	public static final DefaultParticleType GOO = FabricParticleTypes.simple();
	public static final DefaultParticleType BLOOD = FabricParticleTypes.simple();

	@Override
	public void initialize() {
		registerParticle("acid", ACID, AcidParticleFactory::new);
		registerParticle("goo", GOO, GooParticleFactory::new);
		registerParticle("blood", BLOOD, BloodParticleFactory::new);
	}

	private void registerParticle(String path, DefaultParticleType type,
			ParticleFactoryRegistry.PendingParticleFactory<DefaultParticleType> factory) {
		Registry.register(Registry.PARTICLE_TYPE, new Identifier(Gigeresque.MOD_ID, path), type);
		ParticleFactoryRegistry.getInstance().register(type, factory);
	}
}
