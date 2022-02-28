package mods.cybercat.gigeresque.client.entity.render;

import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import mods.cybercat.gigeresque.common.util.InitializationTimer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class EntityRenderers implements GigeresqueInitializer {
	@Override
	public void initialize() {
		InitializationTimer.initializingBlock("EntityRenderers", this::initializeImpl);
	}

	private void initializeImpl() {
		EntityRendererRegistry.register(Entities.ALIEN, AlienEntityRenderer::new);
		EntityRendererRegistry.register(Entities.AQUATIC_ALIEN, AquaticAlienEntityRenderer::new);
		EntityRendererRegistry.register(Entities.AQUATIC_CHESTBURSTER, AquaticChestbursterEntityRenderer::new);
		EntityRendererRegistry.register(Entities.CHESTBURSTER, ChestbursterEntityRenderer::new);
		EntityRendererRegistry.register(Entities.EGG, EggEntityRenderer::new);
		EntityRendererRegistry.register(Entities.FACEHUGGER, FacehuggerEntityRenderer::new);
		EntityRendererRegistry.register(Entities.RUNNER_ALIEN, RunnerAlienEntityRenderer::new);
		EntityRendererRegistry.register(Entities.RUNNERBURSTER, RunnerbursterEntityRenderer::new);
	}
}
