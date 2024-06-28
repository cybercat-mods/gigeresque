package mods.cybercat.gigeresque;

import mod.azure.azurelib.common.internal.common.AzureLib;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.entity.impl.extra.SpitterEntity;
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
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigVillagerTrades;
import mods.cybercat.gigeresque.compat.GigCompats;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;

public final class FabricMod implements ModInitializer {

    @Override
    public void onInitialize() {
        AzureLib.initialize();
        CommonMod.initRegistries();
        FlammableBlockRegistry.getDefaultInstance().add(GigTags.NEST_BLOCKS, 5, 5);
        ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> GigVillagerTrades.addTrades());
        MobSpawn.initialize();
        GigCompats.initialize();
        FabricDefaultAttributeRegistry.register(GigEntities.ALIEN.get(), ClassicAlienEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.AQUATIC_ALIEN.get(), AquaticAlienEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.AQUATIC_CHESTBURSTER.get(), ChestbursterEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.CHESTBURSTER.get(), ChestbursterEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.EGG.get(), AlienEggEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.FACEHUGGER.get(), FacehuggerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.RUNNER_ALIEN.get(), RunnerAlienEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.RUNNERBURSTER.get(), RunnerbursterEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.MUTANT_POPPER.get(), PopperEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.MUTANT_HAMMERPEDE.get(), HammerpedeEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.MUTANT_STALKER.get(), StalkerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.NEOBURSTER.get(), NeobursterEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.NEOMORPH_ADOLESCENT.get(), NeomorphAdolescentEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.NEOMORPH.get(), NeomorphEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.SPITTER.get(), SpitterEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.DRACONICTEMPLEBEAST.get(), DraconicTempleBeastEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.RAVENOUSTEMPLEBEAST.get(), RavenousTempleBeastEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GigEntities.MOONLIGHTHORRORTEMPLEBEAST.get(), MoonlightHorrorTempleBeastEntity.createAttributes());
    }
}
