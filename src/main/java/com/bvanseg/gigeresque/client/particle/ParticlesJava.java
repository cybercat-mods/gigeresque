package com.bvanseg.gigeresque.client.particle;

import com.bvanseg.gigeresque.common.GigeresqueJava;
import com.bvanseg.gigeresque.common.util.GigeresqueInitializerJava;
import com.bvanseg.gigeresque.common.util.InitializationTimer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ParticlesJava implements GigeresqueInitializerJava {
    public static final DefaultParticleType ACID = FabricParticleTypes.simple();
    public static final DefaultParticleType BLOOD = FabricParticleTypes.simple();

    @Override
    public void initialize() {
        InitializationTimer.initializingBlock("Particles", this::initializeImpl);
    }

    private void initializeImpl() {
        registerParticle("acid", ACID, AcidParticleJavaFactory::new);
        registerParticle("blood", BLOOD, BloodParticleJavaFactory::new);
    }

    private void registerParticle(
            String path,
            DefaultParticleType type,
            ParticleFactoryRegistry.PendingParticleFactory<DefaultParticleType> factory
    ) {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(GigeresqueJava.MOD_ID, path), type);
        ParticleFactoryRegistry.getInstance().register(type, factory);
    }
}
