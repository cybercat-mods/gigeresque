package com.bvanseg.gigeresque.common.entity;

import com.bvanseg.gigeresque.common.GigeresqueJava;
import com.bvanseg.gigeresque.common.entity.impl.*;
import com.bvanseg.gigeresque.common.util.GigeresqueInitializerJava;
import com.bvanseg.gigeresque.common.util.InitializationTimer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntitiesJava implements GigeresqueInitializerJava {
    private static <T extends Entity> EntityType<T> registerAlienType(String name, EntityType.EntityFactory<T> factory, float width, float height) {
        return Registry.register(
                Registry.ENTITY_TYPE,
                new Identifier(GigeresqueJava.MOD_ID, name),
                FabricEntityTypeBuilder.create(CustomSpawnGroupJava.ALIEN, factory).dimensions(EntityDimensions.fixed(width, height)).build());
    }

    private static <T extends Entity> EntityType<T> registerAlienType(String name, EntityType.EntityFactory<T> factory, float width) {
        return registerAlienType(name, factory, width, 1.0f);
    }

    private static <T extends Entity> EntityType<T> registerAlienType(String name, EntityType.EntityFactory<T> factory) {
        return registerAlienType(name, factory, 1.0f, 1.0f);
    }

    public static final EntityType<? extends ClassicAlienEntityJava> ALIEN = registerAlienType(EntityIdentifiersJava.ALIEN.getPath(), ClassicAlienEntityJava::new, 1.0f, 2.85f);
    public static final EntityType<? extends AquaticAlienEntityJava> AQUATIC_ALIEN = registerAlienType(EntityIdentifiersJava.AQUATIC_ALIEN.getPath(), AquaticAlienEntityJava::new, 2.0f, 2.0f);
    public static final EntityType<? extends AquaticChestbursterEntityJava> AQUATIC_CHESTBURSTER = registerAlienType(EntityIdentifiersJava.AQUATIC_CHESTBURSTER.getPath(), AquaticChestbursterEntityJava::new, 0.5f, 0.25f);
    public static final EntityType<? extends ChestbursterEntityJava> CHESTBURSTER = registerAlienType(EntityIdentifiersJava.CHESTBURSTER.getPath(), ChestbursterEntityJava::new, 0.5f, 0.25f);
    public static final EntityType<? extends AlienEggEntityJava> EGG = registerAlienType(EntityIdentifiersJava.EGG.getPath(), AlienEggEntityJava::new, 0.7f, 0.7f);
    public static final EntityType<? extends FacehuggerEntityJava> FACEHUGGER = registerAlienType(EntityIdentifiersJava.FACEHUGGER.getPath(), FacehuggerEntityJava::new, 0.5f, 0.3f);
    public static final EntityType<? extends RunnerAlienEntityJava> RUNNER_ALIEN = registerAlienType(EntityIdentifiersJava.RUNNER_ALIEN.getPath(), RunnerAlienEntityJava::new, 1.25f, 1.75f);
    public static final EntityType<? extends RunnerbursterEntityJava> RUNNERBURSTER = registerAlienType(EntityIdentifiersJava.RUNNERBURSTER.getPath(), RunnerbursterEntityJava::new, 0.5f, 0.5f);

    @Override
    public void initialize() {
        InitializationTimer.initializingBlock("AlienTypes", this::initializeImpl);
    }

    private void initializeImpl() {
        FabricDefaultAttributeRegistry.register(ALIEN, ClassicAlienEntityJava.createAttributes());
        FabricDefaultAttributeRegistry.register(AQUATIC_ALIEN, AquaticAlienEntityJava.createAttributes());
        FabricDefaultAttributeRegistry.register(AQUATIC_CHESTBURSTER, AquaticChestbursterEntityJava.createAttributes());
        FabricDefaultAttributeRegistry.register(CHESTBURSTER, ChestbursterEntityJava.createAttributes());
        FabricDefaultAttributeRegistry.register(EGG, AlienEggEntityJava.createAttributes());
        FabricDefaultAttributeRegistry.register(FACEHUGGER, FacehuggerEntityJava.createAttributes());
        FabricDefaultAttributeRegistry.register(RUNNER_ALIEN, RunnerAlienEntityJava.createAttributes());
        FabricDefaultAttributeRegistry.register(RUNNERBURSTER, RunnerbursterEntityJava.createAttributes());
    }
}
