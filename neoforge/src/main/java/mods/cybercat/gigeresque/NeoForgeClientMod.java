package mods.cybercat.gigeresque;

import mod.azure.azurelib.rewrite.render.item.AzItemRendererRegistry;
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

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.render.aqua.AquaticChestbursterEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.blocks.*;
import mods.cybercat.gigeresque.client.entity.render.classic.ChestbursterEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.hellmorphs.BaphomorphEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.items.TrackerItemRenderer;
import mods.cybercat.gigeresque.client.entity.render.misc.AcidEntityRender;
import mods.cybercat.gigeresque.client.entity.render.misc.AmpouleRender;
import mods.cybercat.gigeresque.client.entity.render.misc.AquaEggEntityRender;
import mods.cybercat.gigeresque.client.entity.render.misc.HologramEntityRender;
import mods.cybercat.gigeresque.client.entity.render.misc.SpitterRenderer;
import mods.cybercat.gigeresque.client.entity.render.mutant.StalkerEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.neo.NeomorphRenderer;
import mods.cybercat.gigeresque.client.entity.render.runner.RunnerbursterEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.templebeast.RavenousTempleBeastEntityRenderer;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.client.particle.*;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.item.GigItems;
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
        AzItemRendererRegistry.register(TrackerItemRenderer::new, GigItems.TRACKER.get());
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(GigEntities.ENGINEER_HOLOGRAM.get(), HologramEntityRender::new);
        event.registerEntityRenderer(GigEntities.ACID.get(), AcidEntityRender::new);
        event.registerEntityRenderer(GigEntities.ACID_PROJECTILE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(GigEntities.BLOOD.get(), AcidEntityRender::new);
        event.registerEntityRenderer(GigEntities.GOO.get(), AcidEntityRender::new);
        event.registerEntityRenderer(GigEntities.AMPOULE_PROJECTILE.get(), AmpouleRender::new);
        event.registerEntityRenderer(
            GigEntities.ALIEN.get(),
            mods.cybercat.gigeresque.client.entity.render.classic.AlienEntityRenderer::new
        );
        // event.registerEntityRenderer(GigEntities.ROM_ALIEN.get(), AlienRomEntityRenderer::new);
        event.registerEntityRenderer(
            GigEntities.AQUATIC_ALIEN.get(),
            mods.cybercat.gigeresque.client.entity.render.aqua.AquaticAlienEntityRenderer::new
        );
        event.registerEntityRenderer(GigEntities.AQUATIC_CHESTBURSTER.get(), AquaticChestbursterEntityRenderer::new);
        event.registerEntityRenderer(GigEntities.CHESTBURSTER.get(), ChestbursterEntityRenderer::new);
        event.registerEntityRenderer(GigEntities.EGG.get(), mods.cybercat.gigeresque.client.entity.render.classic.EggEntityRenderer::new);
        event.registerEntityRenderer(
            GigEntities.FACEHUGGER.get(),
            mods.cybercat.gigeresque.client.entity.render.classic.FacehuggerEntityRenderer::new
        );
        event.registerEntityRenderer(
            GigEntities.RUNNER_ALIEN.get(),
            mods.cybercat.gigeresque.client.entity.render.runner.RunnerAlienEntityRenderer::new
        );
        event.registerEntityRenderer(GigEntities.RUNNERBURSTER.get(), RunnerbursterEntityRenderer::new);
        event.registerEntityRenderer(
            GigEntities.MUTANT_POPPER.get(),
            mods.cybercat.gigeresque.client.entity.render.mutant.PopperEntityRenderer::new
        );
        event.registerEntityRenderer(
            GigEntities.MUTANT_HAMMERPEDE.get(),
            mods.cybercat.gigeresque.client.entity.render.mutant.HammerpedeEntityRenderer::new
        );
        event.registerEntityRenderer(GigEntities.MUTANT_STALKER.get(), StalkerEntityRenderer::new);
        event.registerEntityRenderer(
            GigEntities.NEOBURSTER.get(),
            mods.cybercat.gigeresque.client.entity.render.neo.NeobursterRenderer::new
        );
        event.registerEntityRenderer(
            GigEntities.NEOMORPH_ADOLESCENT.get(),
            mods.cybercat.gigeresque.client.entity.render.neo.NeomorphAdolescentRenderer::new
        );
        event.registerEntityRenderer(GigEntities.NEOMORPH.get(), NeomorphRenderer::new);
        event.registerEntityRenderer(GigEntities.SPITTER.get(), SpitterRenderer::new);
        event.registerEntityRenderer(GigEntities.RAVENOUSTEMPLEBEAST.get(), RavenousTempleBeastEntityRenderer::new);
        event.registerEntityRenderer(
            GigEntities.DRACONICTEMPLEBEAST.get(),
            mods.cybercat.gigeresque.client.entity.render.templebeast.DraconicTempleBeastEntityRenderer::new
        );
        event.registerEntityRenderer(
            GigEntities.MOONLIGHTHORRORTEMPLEBEAST.get(),
            mods.cybercat.gigeresque.client.entity.render.templebeast.MoonlightHorrorTempleBeastEntityRenderer::new
        );
        event.registerEntityRenderer(
            GigEntities.HELLMORPH_RUNNER.get(),
            mods.cybercat.gigeresque.client.entity.render.hellmorphs.HellmorphRunnerEntityRenderer::new
        );
        event.registerEntityRenderer(GigEntities.BAPHOMORPH.get(), BaphomorphEntityRenderer::new);
        event.registerEntityRenderer(
            GigEntities.HELL_BURSTER.get(),
            mods.cybercat.gigeresque.client.entity.render.hellmorphs.HellbursterEntityRenderer::new
        );
        event.registerEntityRenderer(GigEntities.AQUA_EGG.get(), AquaEggEntityRender::new);
        event.registerBlockEntityRenderer(
            GigEntities.PETRIFIED_OBJECT.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PetrifiedObjectRender()
        );
        event.registerBlockEntityRenderer(
            GigEntities.PETRIFIED_OBJECT_1.get(),
            (ctx) -> new PetrifiedStatueRender(EntityModels.PETRIFIED_1, EntityTextures.AQUATIC_CHESTBURSTER_PETRIFIED) {}
        );
        event.registerBlockEntityRenderer(
            GigEntities.PETRIFIED_OBJECT_2.get(),
            (ctx) -> new PetrifiedStatueRender(EntityModels.PETRIFIED_2, EntityTextures.CHESTBURSTER_PETRIFIED) {}
        );
        event.registerBlockEntityRenderer(
            GigEntities.PETRIFIED_OBJECT_3.get(),
            (ctx) -> new PetrifiedStatueRender(EntityModels.PETRIFIED_3, EntityTextures.NEOBURSTER_PETRIFIED) {}
        );
        event.registerBlockEntityRenderer(
            GigEntities.PETRIFIED_OBJECT_4.get(),
            (ctx) -> new PetrifiedStatueRender(EntityModels.PETRIFIED_4, EntityTextures.RUNNERBURSTER_PETRIFIED) {}
        );
        event.registerBlockEntityRenderer(
            GigEntities.PETRIFIED_OBJECT_5.get(),
            (ctx) -> new PetrifiedStatueRender(EntityModels.PETRIFIED_5, EntityTextures.SPORE_PETRIFIED) {}
        );
        event.registerBlockEntityRenderer(
            GigEntities.PETRIFIED_STATUE.get(),
            (ctx) -> new PetrifiedStatueRender(EntityModels.PETRIFIED_STATUE, EntityTextures.ALIEN_STASIS) {}
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
