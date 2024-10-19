package mods.cybercat.gigeresque.client.entity.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;

import mods.cybercat.gigeresque.client.entity.render.blocks.*;
import mods.cybercat.gigeresque.client.entity.render.entities.AcidEntityRender;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;

public record GigEntityRenderers() implements GigeresqueInitializer {

    @Override
    public void initialize() {
        EntityRenderers.register(Entities.ACID, AcidEntityRender::new);
        EntityRenderers.register(Entities.GOO, AcidEntityRender::new);
        EntityRenderers.register(Entities.ALIEN, AlienEntityRenderer::new);
        EntityRenderers.register(Entities.AQUATIC_ALIEN, AquaticAlienEntityRenderer::new);
        EntityRenderers.register(Entities.AQUATIC_CHESTBURSTER, AquaticChestbursterEntityRenderer::new);
        EntityRenderers.register(Entities.CHESTBURSTER, ChestbursterEntityRenderer::new);
        EntityRenderers.register(Entities.EGG, EggEntityRenderer::new);
        EntityRenderers.register(Entities.FACEHUGGER, FacehuggerEntityRenderer::new);
        EntityRenderers.register(Entities.RUNNER_ALIEN, RunnerAlienEntityRenderer::new);
        EntityRenderers.register(Entities.RUNNERBURSTER, RunnerbursterEntityRenderer::new);
        EntityRenderers.register(Entities.MUTANT_POPPER, PopperEntityRenderer::new);
        EntityRenderers.register(Entities.MUTANT_HAMMERPEDE, HammerpedeEntityRenderer::new);
        EntityRenderers.register(Entities.MUTANT_STALKER, StalkerEntityRenderer::new);
        EntityRenderers.register(Entities.NEOBURSTER, NeobursterRenderer::new);
        EntityRenderers.register(Entities.NEOMORPH_ADOLESCENT, NeomorphAdolescentRenderer::new);
        EntityRenderers.register(Entities.NEOMORPH, NeomorphRenderer::new);
        EntityRenderers.register(Entities.SPITTER, SpitterRenderer::new);
        EntityRenderers.register(Entities.RAVENOUSTEMPLEBEAST, RavenousTempleBeastEntityRenderer::new);
        EntityRenderers.register(Entities.DRACONICTEMPLEBEAST, DraconicTempleBeastEntityRenderer::new);
        EntityRenderers.register(Entities.MOONLIGHTHORRORTEMPLEBEAST, MoonlightHorrorTempleBeastEntityRenderer::new);
        BlockEntityRenderers.register(
            Entities.PETRIFIED_OBJECT,
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PetrifiedObjectRender()
        );
        BlockEntityRenderers.register(
            Entities.SPORE_ENTITY,
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SporeBlockRender()
        );
        BlockEntityRenderers.register(
            Entities.ALIEN_STORAGE_BLOCK_ENTITY_1,
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusRender()
        );
        BlockEntityRenderers.register(
            Entities.ALIEN_STORAGE_BLOCK_ENTITY_1_GOO,
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusGooRender()
        );
        BlockEntityRenderers.register(
            Entities.ALIEN_STORAGE_BLOCK_ENTITY_1_HUGGER,
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusHuggerRender()
        );
        BlockEntityRenderers.register(
            Entities.ALIEN_STORAGE_BLOCK_ENTITY_1_SPORE,
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusSporeRender()
        );
        BlockEntityRenderers.register(
            Entities.ALIEN_STORAGE_BLOCK_ENTITY_2,
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new JarRender()
        );
        BlockEntityRenderers.register(
            Entities.ALIEN_STORAGE_BLOCK_ENTITY_3,
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SittingIdolRender()
        );
    }
}
