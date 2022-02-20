package com.bvanseg.gigeresque.common.fluid;

import com.bvanseg.gigeresque.common.Gigeresque;
import com.bvanseg.gigeresque.common.util.GigeresqueInitializer;
import com.bvanseg.gigeresque.common.util.InitializationTimer;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Fluids implements GigeresqueInitializer {
	private Fluids() {
	}

	private static Fluids instance;

	synchronized public static Fluids getInstance() {
		if (instance == null) {
			instance = new Fluids();
		}
		return instance;
	}

	private <T extends Fluid> T registerFluid(String path, T fluid) {
		return Registry.register(Registry.FLUID, new Identifier(Gigeresque.MOD_ID, path), fluid);
	}

	public static final BlackFluid BLACK_FLUID_STILL = new BlackFluid.Still();
	public static final BlackFluid BLACK_FLUID_FLOWING = new BlackFluid.Flowing();

	@Override
	public void initialize() {
		InitializationTimer.initializingBlock("Fluids", this::initializeImpl);
	}

	private void initializeImpl() {
		registerFluid("black_fluid_still", BLACK_FLUID_STILL);
		registerFluid("black_fluid_flowing", BLACK_FLUID_FLOWING);
	}
}
