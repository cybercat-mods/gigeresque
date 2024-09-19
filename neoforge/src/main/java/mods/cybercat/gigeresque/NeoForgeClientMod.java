package mods.cybercat.gigeresque;

import mods.cybercat.gigeresque.client.entity.render.*;
import mods.cybercat.gigeresque.client.entity.render.blocks.*;
import mods.cybercat.gigeresque.client.entity.render.entities.AcidEntityRender;
import mods.cybercat.gigeresque.client.particle.AcidParticleFactory;
import mods.cybercat.gigeresque.client.particle.BloodParticleFactory;
import mods.cybercat.gigeresque.client.particle.GooParticleFactory;
import mods.cybercat.gigeresque.client.particle.GigParticles;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.hacky.BlackFluidClientExtensions;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = CommonMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NeoForgeClientMod {

    @SubscribeEvent
    public static void fluidReg(final RegisterClientExtensionsEvent event) {
        event.registerFluidType(new BlackFluidClientExtensions(), NeoForgeMod.BLACKFLUID_TYPE.get());
    }

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(GigBlocks.NEST_RESIN_WEB.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(GigBlocks.NEST_RESIN_WEB_CROSS.get(), RenderType.translucent());
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(GigEntities.ACID.get(), AcidEntityRender::new);
        event.registerEntityRenderer(GigEntities.GOO.get(), AcidEntityRender::new);
        event.registerEntityRenderer(GigEntities.ALIEN.get(), AlienEntityRenderer::new);
        event.registerEntityRenderer(GigEntities.AQUATIC_ALIEN.get(), AquaticAlienEntityRenderer::new);
        event.registerEntityRenderer(GigEntities.AQUATIC_CHESTBURSTER.get(), AquaticChestbursterEntityRenderer::new);
        event.registerEntityRenderer(GigEntities.CHESTBURSTER.get(), ChestbursterEntityRenderer::new);
        event.registerEntityRenderer(GigEntities.EGG.get(), EggEntityRenderer::new);
        event.registerEntityRenderer(GigEntities.FACEHUGGER.get(), FacehuggerEntityRenderer::new);
        event.registerEntityRenderer(GigEntities.RUNNER_ALIEN.get(), RunnerAlienEntityRenderer::new);
        event.registerEntityRenderer(GigEntities.RUNNERBURSTER.get(), RunnerbursterEntityRenderer::new);
        event.registerEntityRenderer(GigEntities.MUTANT_POPPER.get(), PopperEntityRenderer::new);
        event.registerEntityRenderer(GigEntities.MUTANT_HAMMERPEDE.get(), HammerpedeEntityRenderer::new);
        event.registerEntityRenderer(GigEntities.MUTANT_STALKER.get(), StalkerEntityRenderer::new);
        event.registerEntityRenderer(GigEntities.NEOBURSTER.get(), NeobursterRenderer::new);
        event.registerEntityRenderer(GigEntities.NEOMORPH_ADOLESCENT.get(), NeomorphAdolescentRenderer::new);
        event.registerEntityRenderer(GigEntities.NEOMORPH.get(), NeomorphRenderer::new);
        event.registerEntityRenderer(GigEntities.SPITTER.get(), SpitterRenderer::new);
        event.registerEntityRenderer(GigEntities.RAVENOUSTEMPLEBEAST.get(), RavenousTempleBeastEntityRenderer::new);
        event.registerEntityRenderer(GigEntities.DRACONICTEMPLEBEAST.get(), DraconicTempleBeastEntityRenderer::new);
        event.registerEntityRenderer(GigEntities.MOONLIGHTHORRORTEMPLEBEAST.get(), MoonlightHorrorTempleBeastEntityRenderer::new);
        event.registerBlockEntityRenderer(GigEntities.PETRIFIED_OBJECT.get(), (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PetrifiedObjectRender());
        event.registerBlockEntityRenderer(GigEntities.SPORE_ENTITY.get(), (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SporeBlockRender());
        event.registerBlockEntityRenderer(GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_1.get(), (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusRender());
        event.registerBlockEntityRenderer(GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_1_GOO.get(), (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusGooRender());
        event.registerBlockEntityRenderer(GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_1_HUGGER.get(), (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusHuggerRender());
        event.registerBlockEntityRenderer(GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_1_SPORE.get(), (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusSporeRender());
        event.registerBlockEntityRenderer(GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_2.get(), (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new JarRender());
        event.registerBlockEntityRenderer(GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_3.get(), (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SittingIdolRender());
    }

    @SubscribeEvent
    public static void registry(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(GigParticles.ACID.get(), AcidParticleFactory::new);
        event.registerSpriteSet(GigParticles.GOO.get(), GooParticleFactory::new);
        event.registerSpriteSet(GigParticles.BLOOD.get(), BloodParticleFactory::new);
    }
}
