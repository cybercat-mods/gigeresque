package mods.cybercat.gigeresque;

import mod.azure.azurelib.common.animation.cache.AzIdentityRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;

import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.BaphomorphEntity;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.HellbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.HellmorphRunnerEntity;
import mods.cybercat.gigeresque.common.entity.impl.misc.SpitterEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.HammerpedeEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeobursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphAdolescentEntity;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphEntity;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.DraconicTempleBeastEntity;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.MoonlightHorrorTempleBeastEntity;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.RavenousTempleBeastEntity;
import mods.cybercat.gigeresque.common.item.GigItems;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.DispenserBehaviors;
import mods.cybercat.gigeresque.common.worlddata.PandoraData;
import mods.cybercat.gigeresque.common.worlddata.PandoraEffect;

public final class FabricMod implements ModInitializer {

    private final PandoraEffect pandoraEffect = new PandoraEffect();

    @Override
    public void onInitialize() {
        CommonMod.initRegistries();
        FlammableBlockRegistry.getDefaultInstance().add(GigTags.NEST_BLOCKS, 5, 5);
        MobSpawn.initialize();
        FabricDefaultAttributeRegistry.register(GigEntities.ALIEN.get(), ClassicAlienEntity.createAttributes());
        // FabricDefaultAttributeRegistry.register(GigEntities.ROM_ALIEN.get(), RomAlienEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.AQUATIC_ALIEN.get(), AquaticAlienEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(
            GigEntities.AQUATIC_CHESTBURSTER.get(),
            ChestbursterEntity.createAttributes()
        );
        FabricDefaultAttributeRegistry.register(GigEntities.CHESTBURSTER.get(), ChestbursterEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.EGG.get(), AlienEggEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.FACEHUGGER.get(), FacehuggerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.RUNNER_ALIEN.get(), RunnerAlienEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(
            GigEntities.RUNNERBURSTER.get(),
            RunnerbursterEntity.createAttributes()
        );
        FabricDefaultAttributeRegistry.register(GigEntities.MUTANT_POPPER.get(), PopperEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(
            GigEntities.MUTANT_HAMMERPEDE.get(),
            HammerpedeEntity.createAttributes()
        );
        FabricDefaultAttributeRegistry.register(GigEntities.MUTANT_STALKER.get(), StalkerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.NEOBURSTER.get(), NeobursterEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(
            GigEntities.NEOMORPH_ADOLESCENT.get(),
            NeomorphAdolescentEntity.createAttributes()
        );
        FabricDefaultAttributeRegistry.register(GigEntities.NEOMORPH.get(), NeomorphEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.SPITTER.get(), SpitterEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(
            GigEntities.DRACONICTEMPLEBEAST.get(),
            DraconicTempleBeastEntity.createAttributes()
        );
        FabricDefaultAttributeRegistry.register(
            GigEntities.RAVENOUSTEMPLEBEAST.get(),
            RavenousTempleBeastEntity.createAttributes()
        );
        FabricDefaultAttributeRegistry.register(
            GigEntities.MOONLIGHTHORRORTEMPLEBEAST.get(),
            MoonlightHorrorTempleBeastEntity.createAttributes()
        );
        FabricDefaultAttributeRegistry.register(
            GigEntities.HELLMORPH_RUNNER.get(),
            HellmorphRunnerEntity.createAttributes()
        );
        FabricDefaultAttributeRegistry.register(GigEntities.BAPHOMORPH.get(), BaphomorphEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.HELL_BURSTER.get(), HellbursterEntity.createAttributes());
        if (CommonMod.config.enablePandoraEffects) {
            ServerTickEvents.END_WORLD_TICK.register(this::onWorldEndTick);
            ServerTickEvents.START_WORLD_TICK.register(this::onWorldTick);
        }
        AzIdentityRegistry.register(GigItems.TRACKER.get());
        DispenserBehaviors.initialize();
    }

    private void onWorldTick(ServerLevel serverLevel) {
        pandoraEffect.tick(serverLevel, serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING), true);
    }

    private void onWorldEndTick(ServerLevel serverLevel) {
        boolean hasAdvancement = false;

        for (ServerPlayer player : serverLevel.getPlayers(player -> true)) {
            var advancement = player.server.getAdvancements().get(Constants.modResource("xeno_dungeon"));
            if (advancement != null && player.getAdvancements().getOrStartProgress(advancement).isDone()) {
                hasAdvancement = true;
                break;
            }
        }

        if (hasAdvancement) {
            PandoraData.setIsTriggered(true);
        }
    }
}
