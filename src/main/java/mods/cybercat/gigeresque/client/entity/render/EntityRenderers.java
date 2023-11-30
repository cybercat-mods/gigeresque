package mods.cybercat.gigeresque.client.entity.render;

import mods.cybercat.gigeresque.client.entity.render.blocks.*;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

@Environment(EnvType.CLIENT)
public record EntityRenderers() implements GigeresqueInitializer {

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
        EntityRendererRegistry.register(Entities.MUTANT_POPPER, PopperEntityRenderer::new);
        EntityRendererRegistry.register(Entities.MUTANT_HAMMERPEDE, HammerpedeEntityRenderer::new);
        EntityRendererRegistry.register(Entities.MUTANT_STALKER, StalkerEntityRenderer::new);
        EntityRendererRegistry.register(Entities.NEOBURSTER, NeobursterRenderer::new);
        EntityRendererRegistry.register(Entities.NEOMORPH_ADOLESCENT, NeomorphAdolescentRenderer::new);
        EntityRendererRegistry.register(Entities.NEOMORPH, NeomorphRenderer::new);
        EntityRendererRegistry.register(Entities.SPITTER, SpitterRenderer::new);
        EntityRendererRegistry.register(Entities.RAVENOUSTEMPLEBEAST, RavenousTempleBeastEntityRenderer::new);
        EntityRendererRegistry.register(Entities.DRACONICTEMPLEBEAST, DraconicTempleBeastEntityRenderer::new);
        EntityRendererRegistry.register(Entities.MOONLIGHTHORRORTEMPLEBEAST, MoonlightHorrorTempleBeastEntityRenderer::new);
        BlockEntityRenderers.register(Entities.SPORE_ENTITY, (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SporeBlockRender());
        BlockEntityRenderers.register(Entities.ALIEN_STORAGE_BLOCK_ENTITY_1, (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusRender());
        BlockEntityRenderers.register(Entities.ALIEN_STORAGE_BLOCK_ENTITY_1_GOO, (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusGooRender());
        BlockEntityRenderers.register(Entities.ALIEN_STORAGE_BLOCK_ENTITY_1_HUGGER, (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusHuggerRender());
        BlockEntityRenderers.register(Entities.ALIEN_STORAGE_BLOCK_ENTITY_1_SPORE, (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusSporeRender());
        BlockEntityRenderers.register(Entities.ALIEN_STORAGE_BLOCK_ENTITY_2, (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new JarRender());
        BlockEntityRenderers.register(Entities.ALIEN_STORAGE_BLOCK_ENTITY_3, (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SittingIdolRender());
    }
}
