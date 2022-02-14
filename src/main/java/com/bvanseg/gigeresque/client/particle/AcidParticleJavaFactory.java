package com.bvanseg.gigeresque.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class AcidParticleJavaFactory implements ParticleFactory<DefaultParticleType> {
    private final SpriteProvider spriteProvider;

    public AcidParticleJavaFactory(SpriteProvider spriteProvider) {
        this.spriteProvider = spriteProvider;
    }

    @Override
    public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        return new AcidParticleJava(clientWorld, d, e, f, g, h, i, spriteProvider);
    }
}
