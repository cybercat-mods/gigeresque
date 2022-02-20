package com.bvanseg.gigeresque.client.particle;

import com.bvanseg.gigeresque.common.Gigeresque;
import com.bvanseg.gigeresque.common.util.GigeresqueInitializer;
import com.bvanseg.gigeresque.common.util.InitializationTimer;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Particles implements GigeresqueInitializer {
	public static final DefaultParticleType ACID = FabricParticleTypes.simple();
	public static final DefaultParticleType BLOOD = FabricParticleTypes.simple();

	@Override
	public void initialize() {
		InitializationTimer.initializingBlock("Particles", this::initializeImpl);
	}

	private void initializeImpl() {
		registerParticle("acid", ACID, AcidParticleFactory::new);
		registerParticle("blood", BLOOD, BloodParticleFactory::new);
	}

	private void registerParticle(String path, DefaultParticleType type,
			ParticleFactoryRegistry.PendingParticleFactory<DefaultParticleType> factory) {
		Registry.register(Registry.PARTICLE_TYPE, new Identifier(Gigeresque.MOD_ID, path), type);
		ParticleFactoryRegistry.getInstance().register(type, factory);
	}
}
