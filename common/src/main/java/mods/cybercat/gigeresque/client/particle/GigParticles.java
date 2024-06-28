package mods.cybercat.gigeresque.client.particle;

import mods.cybercat.gigeresque.CommonMod;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

public record GigParticles() implements CommonParticleRegistryInterface {
    public static final Supplier<SimpleParticleType> ACID = CommonParticleRegistryInterface.registerParticle(
            CommonMod.MOD_ID, "acid",
            () -> new GigPaticleType(true));
    public static final Supplier<SimpleParticleType> GOO = CommonParticleRegistryInterface.registerParticle(
            CommonMod.MOD_ID, "goo",
            () -> new GigPaticleType(true));
    public static final Supplier<SimpleParticleType> BLOOD = CommonParticleRegistryInterface.registerParticle(
            CommonMod.MOD_ID, "blood",
            () -> new GigPaticleType(true));

    public static void initialize() {
    }
}
