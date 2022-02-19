package com.bvanseg.gigeresque.client.entity.render;

import com.bvanseg.gigeresque.common.entity.EntitiesJava;
import com.bvanseg.gigeresque.common.util.GigeresqueInitializerJava;
import com.bvanseg.gigeresque.common.util.InitializationTimer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class EntityRenderersJava implements GigeresqueInitializerJava {
    @Override
    public void initialize() {
        InitializationTimer.initializingBlock("EntityRenderers", this::initializeImpl);
    }

    private void initializeImpl() {
        EntityRendererRegistry.register(EntitiesJava.ALIEN, AlienEntityRendererJava::new);
        EntityRendererRegistry.register(EntitiesJava.AQUATIC_ALIEN, AquaticAlienEntityRendererJava::new);
        EntityRendererRegistry.register(EntitiesJava.AQUATIC_CHESTBURSTER, AquaticChestbursterEntityRendererJava::new);
        EntityRendererRegistry.register(EntitiesJava.CHESTBURSTER, ChestbursterEntityRendererJava::new);
        EntityRendererRegistry.register(EntitiesJava.EGG, EggEntityRendererJava::new);
        EntityRendererRegistry.register(EntitiesJava.FACEHUGGER, FacehuggerEntityRendererJava::new);
        EntityRendererRegistry.register(EntitiesJava.RUNNER_ALIEN, RunnerAlienEntityRendererJava::new);
        EntityRendererRegistry.register(EntitiesJava.RUNNERBURSTER, RunnerbursterEntityRendererJava::new);
    }
}
