package mods.cybercat.gigeresque.common.fluid;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

public record GigFluids() implements GigeresqueInitializer {

    public static final BlackFluid BLACK_FLUID_STILL = new BlackFluid.Still();
    public static final BlackFluid BLACK_FLUID_FLOWING = new BlackFluid.Flowing();
    private static GigFluids instance;

    public static synchronized GigFluids getInstance() {
        if (instance == null) {
            instance = new GigFluids();
        }
        return instance;
    }

    private <T extends Fluid> T registerFluid(String path, T fluid) {
        return Registry.register(BuiltInRegistries.FLUID, new ResourceLocation(Gigeresque.MOD_ID, path), fluid);
    }

    @Override
    public void initialize() {
        registerFluid("black_fluid_still", BLACK_FLUID_STILL);
        registerFluid("black_fluid_flowing", BLACK_FLUID_FLOWING);
    }
}
