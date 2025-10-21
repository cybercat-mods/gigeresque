package mods.cybercat.gigeresque;

import mod.azure.azurelib.common.render.item.AzItemRendererRegistry;
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
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.Items;

import mods.cybercat.gigeresque.client.FluidRenderHandlers;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.render.aqua.AquaticChestbursterEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.blocks.*;
import mods.cybercat.gigeresque.client.entity.render.classic.AlienEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.classic.ChestbursterEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.classic.EggEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.classic.FacehuggerEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.feature.HeldItemLayer;
import mods.cybercat.gigeresque.client.entity.render.hellmorphs.BaphomorphEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.hellmorphs.HellbursterEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.hellmorphs.HellmorphRunnerEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.items.SporeItemBlockRender;
import mods.cybercat.gigeresque.client.entity.render.items.TrackerItemRenderer;
import mods.cybercat.gigeresque.client.entity.render.misc.*;
import mods.cybercat.gigeresque.client.entity.render.misc.AquaEggEntityRender;
import mods.cybercat.gigeresque.client.entity.render.misc.HologramEntityRender;
import mods.cybercat.gigeresque.client.entity.render.misc.SpitterRenderer;
import mods.cybercat.gigeresque.client.entity.render.mutant.HammerpedeEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.mutant.PopperEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.mutant.StalkerEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.neo.NeobursterRenderer;
import mods.cybercat.gigeresque.client.entity.render.neo.NeomorphAdolescentRenderer;
import mods.cybercat.gigeresque.client.entity.render.neo.NeomorphRenderer;
import mods.cybercat.gigeresque.client.entity.render.runner.RunnerAlienEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.runner.RunnerbursterEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.templebeast.DraconicTempleBeastEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.templebeast.MoonlightHorrorTempleBeastEntityRenderer;
import mods.cybercat.gigeresque.client.entity.render.templebeast.RavenousTempleBeastEntityRenderer;
import mods.cybercat.gigeresque.client.entity.texture.BlockModels;
import mods.cybercat.gigeresque.client.entity.texture.BlockTextures;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.client.particle.*;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.block.animators.*;
import mods.cybercat.gigeresque.common.block.entity.*;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.item.GigItems;
import mods.cybercat.gigeresque.common.predicates.*;

public class FabricModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FluidRenderHandlers.initialize();
        BlockRenderLayerMap.INSTANCE.putBlock(GigBlocks.NEST_RESIN_WEB.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GigBlocks.NEST_RESIN_WEB_CROSS.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(GigBlocks.BEACON_BLOCK.get(), RenderType.translucent());
        registerParticle(GigParticles.ACID.get(), AcidParticleFactory::new);
        registerParticle(GigParticles.GOO.get(), GooParticleFactory::new);
        registerParticle(GigParticles.BLOOD.get(), BloodParticleFactory::new);
        registerParticle(GigParticles.MIST.get(), MistParticleFactory::new);
        EntityRenderers.register(GigEntities.ENGINEER_HOLOGRAM.get(), HologramEntityRender::new);
        EntityRenderers.register(GigEntities.ACID.get(), AcidEntityRender::new);
        EntityRenderers.register(GigEntities.ACID_PROJECTILE.get(), ThrownItemRenderer::new);
        EntityRenderers.register(GigEntities.BLOOD.get(), AcidEntityRender::new);
        EntityRenderers.register(GigEntities.GOO.get(), AcidEntityRender::new);
        EntityRenderers.register(GigEntities.AMPOULE_PROJECTILE.get(), AmpouleRender::new);
        EntityRenderers.register(GigEntities.ALIEN.get(), AlienEntityRenderer::new);
        // EntityRenderers.register(GigEntities.ROM_ALIEN.get(), AlienRomEntityRenderer::new);
        EntityRenderers.register(
            GigEntities.AQUATIC_ALIEN.get(),
            mods.cybercat.gigeresque.client.entity.render.aqua.AquaticAlienEntityRenderer::new
        );
        EntityRenderers.register(GigEntities.AQUATIC_CHESTBURSTER.get(), AquaticChestbursterEntityRenderer::new);
        EntityRenderers.register(
            GigEntities.CHESTBURSTER.get(),
            ChestbursterEntityRenderer::new
        );
        EntityRenderers.register(GigEntities.EGG.get(), EggEntityRenderer::new);
        EntityRenderers.register(GigEntities.FACEHUGGER.get(), FacehuggerEntityRenderer::new);
        EntityRenderers.register(
            GigEntities.RUNNER_ALIEN.get(),
            RunnerAlienEntityRenderer::new
        );
        EntityRenderers.register(GigEntities.RUNNERBURSTER.get(), RunnerbursterEntityRenderer::new);
        EntityRenderers.register(
            GigEntities.MUTANT_POPPER.get(),
            PopperEntityRenderer::new
        );
        EntityRenderers.register(
            GigEntities.MUTANT_HAMMERPEDE.get(),
            HammerpedeEntityRenderer::new
        );
        EntityRenderers.register(GigEntities.MUTANT_STALKER.get(), StalkerEntityRenderer::new);
        EntityRenderers.register(GigEntities.NEOBURSTER.get(), NeobursterRenderer::new);
        EntityRenderers.register(
            GigEntities.NEOMORPH_ADOLESCENT.get(),
            NeomorphAdolescentRenderer::new
        );
        EntityRenderers.register(GigEntities.NEOMORPH.get(), NeomorphRenderer::new);
        EntityRenderers.register(GigEntities.SPITTER.get(), SpitterRenderer::new);
        EntityRenderers.register(GigEntities.RAVENOUSTEMPLEBEAST.get(), RavenousTempleBeastEntityRenderer::new);
        EntityRenderers.register(
            GigEntities.DRACONICTEMPLEBEAST.get(),
            DraconicTempleBeastEntityRenderer::new
        );
        EntityRenderers.register(
            GigEntities.MOONLIGHTHORRORTEMPLEBEAST.get(),
            MoonlightHorrorTempleBeastEntityRenderer::new
        );
        EntityRenderers.register(
            GigEntities.HELLMORPH_RUNNER.get(),
            HellmorphRunnerEntityRenderer::new
        );
        EntityRenderers.register(GigEntities.BAPHOMORPH.get(), BaphomorphEntityRenderer::new);
        EntityRenderers.register(
            GigEntities.HELL_BURSTER.get(),
            HellbursterEntityRenderer::new
        );
        EntityRenderers.register(GigEntities.AQUA_EGG.get(), AquaEggEntityRender::new);
        BlockEntityRenderers.register(
            GigEntities.PETRIFIED_OBJECT.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new BaseBlockRenderer<>(
                BlockModels.EGG_PETRIFIED_MODEL,
                EntityTextures.EGG_PETRIFIED,
                PetrifiedAnimator::new
            ) {}
        );
        BlockEntityRenderers.register(
            GigEntities.PETRIFIED_OBJECT_1.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new BaseBlockRenderer<>(
                EntityModels.PETRIFIED_1,
                EntityTextures.AQUATIC_CHESTBURSTER_PETRIFIED
            ) {}
        );
        BlockEntityRenderers.register(
            GigEntities.PETRIFIED_OBJECT_2.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new BaseBlockRenderer<>(
                EntityModels.PETRIFIED_2,
                EntityTextures.CHESTBURSTER_PETRIFIED
            ) {}
        );
        BlockEntityRenderers.register(
            GigEntities.PETRIFIED_OBJECT_3.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new BaseBlockRenderer<>(
                EntityModels.PETRIFIED_3,
                EntityTextures.NEOBURSTER_PETRIFIED
            ) {}
        );
        BlockEntityRenderers.register(
            GigEntities.PETRIFIED_OBJECT_4.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new BaseBlockRenderer<>(
                EntityModels.PETRIFIED_4,
                EntityTextures.RUNNERBURSTER_PETRIFIED
            ) {}
        );
        BlockEntityRenderers.register(
            GigEntities.PETRIFIED_OBJECT_5.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new BaseBlockRenderer<>(
                EntityModels.PETRIFIED_5,
                EntityTextures.SPORE_PETRIFIED
            ) {}
        );
        BlockEntityRenderers.register(
            GigEntities.PETRIFIED_STATUE.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new BaseBlockRenderer<>(
                EntityModels.PETRIFIED_STATUE,
                EntityTextures.ALIEN_STASIS
            ) {}
        );
        BlockEntityRenderers.register(
            GigEntities.SPORE_ENTITY.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new BaseBlockRenderer<>(
                BlockModels.SPORE_MODEL,
                BlockTextures.SPORE_TEXTURE,
                SporeAnimator::new
            ) {}
        );
        BlockEntityRenderers.register(
            GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_1.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new BaseBlockRenderer<AlienStorageEntity>(
                BlockModels.SARCOPHAGUS_MODEL,
                BlockTextures.SARCOPHAGUS_TEXTURE,
                StatueAnimator::new
            ) {}
        );
        BlockEntityRenderers.register(
            GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_1_GOO.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new BaseBlockRenderer<AlienStorageGooEntity>(
                BlockModels.SARCOPHAGUS_MODEL,
                BlockTextures.SARCOPHAGUS_TEXTURE,
                StatueAnimator::new
            ) {}
        );
        BlockEntityRenderers.register(
            GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_1_HUGGER.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new BaseBlockRenderer<AlienStorageHuggerEntity>(
                BlockModels.SARCOPHAGUS_MODEL,
                BlockTextures.SARCOPHAGUS_TEXTURE,
                StatueAnimator::new
            ) {}
        );
        BlockEntityRenderers.register(
            GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_1_SPORE.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new BaseBlockRenderer<AlienStorageSporeEntity>(
                BlockModels.SARCOPHAGUS_MODEL,
                BlockTextures.SARCOPHAGUS_TEXTURE,
                StatueAnimator::new
            ) {}
        );
        BlockEntityRenderers.register(
            GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_2.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new BaseBlockRenderer<JarStorageEntity>(
                BlockModels.JAR_MODEL,
                BlockTextures.JAR_TEXTURE,
                JarAnimator::new
            ) {}
        );
        BlockEntityRenderers.register(
            GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_3.get(),
            (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new BaseBlockRenderer<IdolStorageEntity>(
                BlockModels.SITTING_IDOL_MODEL,
                BlockTextures.SITTING_IDOL_TEXTURE,
                SittingIdolAnimator::new,
                new HeldItemLayer<>()
            ) {}
        );
        AzItemRendererRegistry.register(TrackerItemRenderer::new, GigItems.TRACKER.get());
        AzItemRendererRegistry.register(SporeItemBlockRender::new, GigBlocks.SPORE_BLOCK.get().asItem());
        ClientPlayConnectionEvents.JOIN.register(this::onJoin);

        // Predicate stuff
        ItemProperties.register(
            Items.CROSSBOW,
            Constants.modResource("acid_loaded"),
            AmpoulePredicates::acidLoadedPredicate
        );

        ItemProperties.register(
            Items.CROSSBOW,
            Constants.modResource("goo_loaded"),
            AmpoulePredicates::gooLoadedPredicate
        );
    }

    private void registerParticle(
        SimpleParticleType type,
        ParticleFactoryRegistry.PendingParticleFactory<SimpleParticleType> factory
    ) {
        ParticleFactoryRegistry.getInstance().register(type, factory);
    }

    private void onJoin(ClientPacketListener clientPacketListener, PacketSender packetSender, Minecraft minecraft) {
        Constants.particleCount = 0;
    }
}
