package mods.cybercat.gigeresque;

import mod.azure.azurelib.rewrite.render.item.AzItemRendererRegistry;
import mods.cybercat.gigeresque.client.entity.render.items.SporeItemBlockRender;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

import mods.cybercat.gigeresque.client.FluidRenderHandlers;
import mods.cybercat.gigeresque.client.entity.render.*;
import mods.cybercat.gigeresque.client.entity.render.blocks.*;
import mods.cybercat.gigeresque.client.entity.render.entities.AcidEntityRender;
import mods.cybercat.gigeresque.client.entity.render.entities.HologramEntityRender;
import mods.cybercat.gigeresque.client.entity.render.items.TrackerItemRenderer;
import mods.cybercat.gigeresque.client.particle.*;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.item.GigItems;

public class FabricModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FluidRenderHandlers.initialize();
        BlockRenderLayerMap.INSTANCE.putBlock(GigBlocks.NEST_RESIN_WEB.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GigBlocks.NEST_RESIN_WEB_CROSS.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(GigBlocks.BEACON_BLOCK.get(), RenderType.translucent());
        registerParticle("acid", GigParticles.ACID.get(), AcidParticleFactory::new);
        registerParticle("goo", GigParticles.GOO.get(), GooParticleFactory::new);
        registerParticle("blood", GigParticles.BLOOD.get(), BloodParticleFactory::new);
        registerParticle("mist", GigParticles.MIST.get(), MistParticleFactory::new);
        EntityRenderers.register(GigEntities.ENGINEER_HOLOGRAM.get(), HologramEntityRender::new);
        EntityRenderers.register(GigEntities.ACID.get(), AcidEntityRender::new);
        EntityRenderers.register(GigEntities.ACID_PROJECTILE.get(), ThrownItemRenderer::new);
        EntityRenderers.register(GigEntities.GOO.get(), AcidEntityRender::new);
        EntityRenderers.register(GigEntities.ALIEN.get(), AlienEntityRenderer::new);
        // EntityRenderers.register(GigEntities.ROM_ALIEN.get(), AlienRomEntityRenderer::new);
        EntityRenderers.register(GigEntities.AQUATIC_ALIEN.get(), AquaticAlienEntityRenderer::new);
        EntityRenderers.register(GigEntities.AQUATIC_CHESTBURSTER.get(), AquaticChestbursterEntityRenderer::new);
        EntityRenderers.register(GigEntities.CHESTBURSTER.get(), ChestbursterEntityRenderer::new);
        EntityRenderers.register(GigEntities.EGG.get(), EggEntityRenderer::new);
        EntityRenderers.register(GigEntities.FACEHUGGER.get(), FacehuggerEntityRenderer::new);
        EntityRenderers.register(GigEntities.RUNNER_ALIEN.get(), RunnerAlienEntityRenderer::new);
        EntityRenderers.register(GigEntities.RUNNERBURSTER.get(), RunnerbursterEntityRenderer::new);
        EntityRenderers.register(GigEntities.MUTANT_POPPER.get(), PopperEntityRenderer::new);
        EntityRenderers.register(GigEntities.MUTANT_HAMMERPEDE.get(), HammerpedeEntityRenderer::new);
        EntityRenderers.register(GigEntities.MUTANT_STALKER.get(), StalkerEntityRenderer::new);
        EntityRenderers.register(GigEntities.NEOBURSTER.get(), NeobursterRenderer::new);
        EntityRenderers.register(GigEntities.NEOMORPH_ADOLESCENT.get(), NeomorphAdolescentRenderer::new);
        EntityRenderers.register(GigEntities.NEOMORPH.get(), NeomorphRenderer::new);
        EntityRenderers.register(GigEntities.SPITTER.get(), SpitterRenderer::new);
        EntityRenderers.register(GigEntities.RAVENOUSTEMPLEBEAST.get(), RavenousTempleBeastEntityRenderer::new);
        EntityRenderers.register(GigEntities.DRACONICTEMPLEBEAST.get(), DraconicTempleBeastEntityRenderer::new);
        EntityRenderers.register(GigEntities.MOONLIGHTHORRORTEMPLEBEAST.get(), MoonlightHorrorTempleBeastEntityRenderer::new);
        EntityRenderers.register(GigEntities.HELLMORPH_RUNNER.get(), HellmorphRunnerEntityRenderer::new);
        EntityRenderers.register(GigEntities.BAPHOMORPH.get(), BaphomorphEntityRenderer::new);
        EntityRenderers.register(GigEntities.HELL_BURSTER.get(), HellbursterEntityRenderer::new);
        EntityRenderers.register(GigEntities.AQUA_EGG.get(), AquaEggEntityRender::new);
        BlockEntityRenderers.register(
            GigEntities.PETRIFIED_OBJECT.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PetrifiedObjectRender()
        );
        BlockEntityRenderers.register(
            GigEntities.PETRIFIED_OBJECT_1.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PetrifiedObject1Render()
        );
        BlockEntityRenderers.register(
            GigEntities.PETRIFIED_OBJECT_2.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PetrifiedObject2Render()
        );
        BlockEntityRenderers.register(
            GigEntities.PETRIFIED_OBJECT_3.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PetrifiedObject3Render()
        );
        BlockEntityRenderers.register(
            GigEntities.PETRIFIED_OBJECT_4.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PetrifiedObject4Render()
        );
        BlockEntityRenderers.register(
            GigEntities.PETRIFIED_OBJECT_5.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new PetrifiedObject5Render()
        );
        BlockEntityRenderers.register(
            GigEntities.SPORE_ENTITY.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SporeBlockRender()
        );
        BlockEntityRenderers.register(
            GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_1.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusRender()
        );
        BlockEntityRenderers.register(
            GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_1_GOO.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusGooRender()
        );
        BlockEntityRenderers.register(
            GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_1_HUGGER.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusHuggerRender()
        );
        BlockEntityRenderers.register(
            GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_1_SPORE.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SarcophagusSporeRender()
        );
        BlockEntityRenderers.register(
            GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_2.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new JarRender()
        );
        BlockEntityRenderers.register(
            GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_3.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new SittingIdolRender()
        );
        AzItemRendererRegistry.register(TrackerItemRenderer::new, GigItems.TRACKER.get());
        AzItemRendererRegistry.register(SporeItemBlockRender::new, GigBlocks.SPORE_BLOCK.get().asItem());
        ClientPlayConnectionEvents.JOIN.register(this::onJoin);
    }

    private void registerParticle(
        String path,
        SimpleParticleType type,
        ParticleFactoryRegistry.PendingParticleFactory<SimpleParticleType> factory
    ) {
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Constants.modResource(path), type);
        ParticleFactoryRegistry.getInstance().register(type, factory);
    }

    private void onJoin(ClientPacketListener clientPacketListener, PacketSender packetSender, Minecraft minecraft) {
        Constants.particleCount = 0;
    }
}
