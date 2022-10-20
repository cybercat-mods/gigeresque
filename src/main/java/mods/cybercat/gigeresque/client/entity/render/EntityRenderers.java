package mods.cybercat.gigeresque.client.entity.render;

import mods.cybercat.gigeresque.client.entity.render.blocks.JarRender;
import mods.cybercat.gigeresque.client.entity.render.blocks.SarcophagusRender;
import mods.cybercat.gigeresque.client.entity.render.blocks.SittingIdolRender;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

@Environment(EnvType.CLIENT)
public class EntityRenderers implements GigeresqueInitializer {
	
	@Override
	public void initialize() {
		EntityRendererRegistry.register(Entities.ALIEN, AlienEntityRenderer::new);
		EntityRendererRegistry.register(Entities.AQUATIC_ALIEN, AquaticAlienEntityRenderer::new);
		EntityRendererRegistry.register(Entities.AQUATIC_CHESTBURSTER, AquaticChestbursterEntityRenderer::new);
		EntityRendererRegistry.register(Entities.CHESTBURSTER, ChestbursterEntityRenderer::new);
		EntityRendererRegistry.register(Entities.EGG, EggEntityRenderer::new);
		EntityRendererRegistry.register(Entities.FACEHUGGER, FacehuggerEntityRenderer::new);
		EntityRendererRegistry.register(Entities.RUNNER_ALIEN, RunnerAlienEntityRenderer::new);
		EntityRendererRegistry.register(Entities.RUNNERBURSTER, RunnerbursterEntityRenderer::new);
		BlockEntityRendererRegistry.register(Entities.ALIEN_STORAGE_BLOCK_ENTITY_1,
				(BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusRender());
		BlockEntityRendererRegistry.register(Entities.ALIEN_STORAGE_BLOCK_ENTITY_2,
				(BlockEntityRendererProvider.Context rendererDispatcherIn) -> new JarRender());
		BlockEntityRendererRegistry.register(Entities.ALIEN_STORAGE_BLOCK_ENTITY_3,
				(BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SittingIdolRender());
	}
}
