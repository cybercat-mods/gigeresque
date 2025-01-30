package mods.cybercat.gigeresque;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import mods.cybercat.gigeresque.client.entity.render.*;
import mods.cybercat.gigeresque.client.entity.render.blocks.*;
import mods.cybercat.gigeresque.client.entity.render.entities.AcidEntityRender;
import mods.cybercat.gigeresque.client.entity.render.entities.HologramEntityRender;
import mods.cybercat.gigeresque.client.particle.*;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.hacky.BlackFluidClientExtensions;

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
        ItemBlockRenderTypes.setRenderLayer(GigBlocks.BEACON_BLOCK.get(), RenderType.translucent());
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(GigEntities.ENGINEER_HOLOGRAM.get(), HologramEntityRender::new);
        event.registerEntityRenderer(GigEntities.ACID.get(), AcidEntityRender::new);
        event.registerEntityRenderer(GigEntities.ACID_PROJECTILE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(GigEntities.GOO.get(), AcidEntityRender::new);
        event.registerEntityRenderer(GigEntities.ALIEN.get(), AlienEntityRenderer::new);
        // event.registerEntityRenderer(GigEntities.ROM_ALIEN.get(), AlienRomEntityRenderer::new);
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
        event.registerEntityRenderer(GigEntities.HELLMORPH_RUNNER.get(), HellmorphRunnerEntityRenderer::new);
        event.registerEntityRenderer(GigEntities.BAPHOMORPH.get(), BaphomorphEntityRenderer::new);
        event.registerEntityRenderer(GigEntities.HELL_BURSTER.get(), HellbursterEntityRenderer::new);
        event.registerEntityRenderer(GigEntities.AQUA_EGG.get(), AquaEggEntityRender::new);
        event.registerBlockEntityRenderer(
            GigEntities.PETRIFIED_OBJECT.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PetrifiedObjectRender()
        );
        event.registerBlockEntityRenderer(
            GigEntities.PETRIFIED_OBJECT_1.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PetrifiedObject1Render()
        );
        event.registerBlockEntityRenderer(
            GigEntities.PETRIFIED_OBJECT_2.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PetrifiedObject2Render()
        );
        event.registerBlockEntityRenderer(
            GigEntities.PETRIFIED_OBJECT_3.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PetrifiedObject3Render()
        );
        event.registerBlockEntityRenderer(
            GigEntities.PETRIFIED_OBJECT_4.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PetrifiedObject4Render()
        );
        event.registerBlockEntityRenderer(
            GigEntities.PETRIFIED_OBJECT_5.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PetrifiedObject5Render()
        );
        event.registerBlockEntityRenderer(
            GigEntities.SPORE_ENTITY.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SporeBlockRender()
        );
        event.registerBlockEntityRenderer(
            GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_1.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusRender()
        );
        event.registerBlockEntityRenderer(
            GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_1_GOO.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusGooRender()
        );
        event.registerBlockEntityRenderer(
            GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_1_HUGGER.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusHuggerRender()
        );
        event.registerBlockEntityRenderer(
            GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_1_SPORE.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusSporeRender()
        );
        event.registerBlockEntityRenderer(
            GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_2.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new JarRender()
        );
        event.registerBlockEntityRenderer(
            GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_3.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SittingIdolRender()
        );
    }

    @SubscribeEvent
    public static void registry(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(GigParticles.ACID.get(), AcidParticleFactory::new);
        event.registerSpriteSet(GigParticles.GOO.get(), GooParticleFactory::new);
        event.registerSpriteSet(GigParticles.BLOOD.get(), BloodParticleFactory::new);
        event.registerSpriteSet(GigParticles.MIST.get(), MistParticleFactory::new);
    }
}
