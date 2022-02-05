package com.bvanseg.gigeresque.common.fluid;

import com.bvanseg.gigeresque.common.GigeresqueJava;
import com.bvanseg.gigeresque.common.util.GigeresqueInitializerJava;
import com.bvanseg.gigeresque.common.util.InitializationTimer;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluidsJava implements GigeresqueInitializerJava {
    private FluidsJava() {
    }

    private <T extends Fluid> T registerFluid(String path, T fluid) {
        return Registry.register(Registry.FLUID, new Identifier(GigeresqueJava.MOD_ID, path), fluid);
    }

    public static final BlackFluidJava BLACK_FLUID_STILL = new BlackFluidJava.Still();
    static final BlackFluidJava BLACK_FLUID_FLOWING = new BlackFluidJava.Flowing();

    @Override
    public void initialize() {
        InitializationTimer.initializingBlock("Fluids", this::initializeImpl);
    }

    private void initializeImpl() {
        registerFluid("black_fluid_still", BLACK_FLUID_STILL);
        registerFluid("black_fluid_flowing", BLACK_FLUID_FLOWING);
    }
}
