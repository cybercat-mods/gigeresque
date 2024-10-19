package mods.cybercat.gigeresque.client.particle;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;

public record Particles() implements GigeresqueInitializer {

    public static final SimpleParticleType ACID = FabricParticleTypes.simple();

    public static final SimpleParticleType GOO = FabricParticleTypes.simple();

    public static final SimpleParticleType BLOOD = FabricParticleTypes.simple();

    @Override
    public void initialize() {
        registerParticle("acid", ACID, AcidParticleFactory::new);
        registerParticle("goo", GOO, GooParticleFactory::new);
        registerParticle("blood", BLOOD, BloodParticleFactory::new);
    }

    private void registerParticle(
        String path,
        SimpleParticleType type,
        ParticleFactoryRegistry.PendingParticleFactory<SimpleParticleType> factory
    ) {
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Constants.modResource(path), type);
        ParticleFactoryRegistry.getInstance().register(type, factory);
    }
}
