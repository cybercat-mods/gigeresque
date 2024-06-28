package mods.cybercat.gigeresque.common.fluid;

import mods.cybercat.gigeresque.CommonMod;

import java.util.function.Supplier;

public class GigFluids implements CommonFluidRegistryInterface {
    public static final Supplier<BlackFluid> BLACK_FLUID_STILL = CommonFluidRegistryInterface.registerFluid(CommonMod.MOD_ID, "black_fluid_still", BlackFluid.Still::new);
    public static final Supplier<BlackFluid> BLACK_FLUID_FLOWING = CommonFluidRegistryInterface.registerFluid(CommonMod.MOD_ID, "black_fluid_flowing", BlackFluid.Flowing::new);

    public static void initialize() {
    }
}