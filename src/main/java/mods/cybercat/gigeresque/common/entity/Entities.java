package mods.cybercat.gigeresque.common.entity;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.block.entity.*;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.entity.impl.extra.*;
import mods.cybercat.gigeresque.common.entity.impl.mutant.HammerpedeEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeobursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphAdolescentEntity;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphEntity;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerbursterEntity;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public record Entities() implements GigeresqueInitializer {

    public static final EntityType<? extends ClassicAlienEntity> ALIEN = registerEntityType(
            EntityIdentifiers.ALIEN.getPath(), ClassicAlienEntity::new, MobCategory.MONSTER, 0.9f, 2.45f);
    public static final EntityType<? extends AquaticAlienEntity> AQUATIC_ALIEN = registerEntityType(
            EntityIdentifiers.AQUATIC_ALIEN.getPath(), AquaticAlienEntity::new, MobCategory.MONSTER, 2.0f, 2.0f);
    public static final EntityType<? extends AquaticChestbursterEntity> AQUATIC_CHESTBURSTER = registerEntityType(
            EntityIdentifiers.AQUATIC_CHESTBURSTER.getPath(), AquaticChestbursterEntity::new, MobCategory.MONSTER, 0.5f,
            0.25f);
    public static final EntityType<? extends ChestbursterEntity> CHESTBURSTER = registerEntityType(
            EntityIdentifiers.CHESTBURSTER.getPath(), ChestbursterEntity::new, MobCategory.MONSTER, 0.5f, 0.25f);
    public static final EntityType<? extends AlienEggEntity> EGG = registerEntityType(EntityIdentifiers.EGG.getPath(),
            AlienEggEntity::new, MobCategory.MONSTER, 0.7f, 0.9f);
    public static final EntityType<? extends FacehuggerEntity> FACEHUGGER = registerEntityType(
            EntityIdentifiers.FACEHUGGER.getPath(), FacehuggerEntity::new, MobCategory.MONSTER, 0.95f, 0.3f);
    public static final EntityType<? extends RunnerAlienEntity> RUNNER_ALIEN = registerEntityType(
            EntityIdentifiers.RUNNER_ALIEN.getPath(), RunnerAlienEntity::new, MobCategory.MONSTER, 1.25f, 1.75f);
    public static final EntityType<? extends RunnerbursterEntity> RUNNERBURSTER = registerEntityType(
            EntityIdentifiers.RUNNERBURSTER.getPath(), RunnerbursterEntity::new, MobCategory.MONSTER, 0.5f, 0.5f);
    public static final EntityType<? extends PopperEntity> MUTANT_POPPER = registerEntityType(
            EntityIdentifiers.MUTANT_POPPER.getPath(), PopperEntity::new, MobCategory.MONSTER, 1.0f, 0.75f);
    public static final EntityType<? extends HammerpedeEntity> MUTANT_HAMMERPEDE = registerEntityType(
            EntityIdentifiers.MUTANT_HAMMERPEDE.getPath(), HammerpedeEntity::new, MobCategory.MONSTER, 1.4f, 0.75f);
    public static final EntityType<? extends StalkerEntity> MUTANT_STALKER = registerEntityType(
            EntityIdentifiers.MUTANT_STALKER.getPath(), StalkerEntity::new, MobCategory.MONSTER, 1.25f, 1.75f);
    public static final EntityType<? extends NeobursterEntity> NEOBURSTER = registerEntityType(
            EntityIdentifiers.NEOBURSTER.getPath(), NeobursterEntity::new, MobCategory.MONSTER, 0.5f, 0.45f);
    public static final EntityType<? extends NeomorphAdolescentEntity> NEOMORPH_ADOLESCENT = registerEntityType(
            EntityIdentifiers.NEOMORPH_ADOLESCENT.getPath(), NeomorphAdolescentEntity::new, MobCategory.MONSTER, 1.0f,
            0.9f);
    public static final EntityType<? extends NeomorphEntity> NEOMORPH = registerEntityType(
            EntityIdentifiers.NEOMORPH.getPath(), NeomorphEntity::new, MobCategory.MONSTER, 0.9f, 2.55f);
    public static final EntityType<? extends SpitterEntity> SPITTER = registerEntityType(
            EntityIdentifiers.SPITTER.getPath(), SpitterEntity::new, MobCategory.MONSTER, 0.9f, 2.0f);
    public static final EntityType<? extends DraconicTempleBeastEntity> DRACONICTEMPLEBEAST = registerEntityType(
            EntityIdentifiers.DRACONICTEMPLEBEAST.getPath(), DraconicTempleBeastEntity::new, MobCategory.MONSTER, 1.9f,
            2.35f);
    public static final EntityType<? extends RavenousTempleBeastEntity> RAVENOUSTEMPLEBEAST = registerEntityType(
            EntityIdentifiers.RAVENOUSTEMPLEBEAST.getPath(), RavenousTempleBeastEntity::new, MobCategory.MONSTER, 1.6f,
            3.95f);
    public static final EntityType<? extends MoonlightHorrorTempleBeastEntity> MOONLIGHTHORRORTEMPLEBEAST = registerEntityType(
            EntityIdentifiers.MOONLIGHTHORRORTEMPLEBEAST.getPath(), MoonlightHorrorTempleBeastEntity::new,
            MobCategory.MONSTER, 2.1f, 4.95f);
    public static final EntityType<? extends AcidEntity> ACID = registerEntityType(EntityIdentifiers.ACID.getPath(),
            AcidEntity::new, MobCategory.MISC, 0.8f, 0.05f);
    public static final EntityType<? extends GooEntity> GOO = registerEntityType(EntityIdentifiers.GOO.getPath(),
            GooEntity::new, MobCategory.MISC, 0.8f, 0.05f);
    private static Entities instance;

    public static synchronized Entities getInstance() {
        if (instance == null) instance = new Entities();
        return instance;
    }

    private static <T extends Entity> EntityType<T> registerEntityType(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, Constants.modResource(name),
                FabricEntityTypeBuilder.create(category, factory).dimensions(
                        EntityDimensions.fixed(width, height)).trackedUpdateRate(1).build());
    }

    private static <T extends BlockEntity> BlockEntityType<T> registerBlockType(String name, FabricBlockEntityTypeBuilder.Factory<T> factory, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.modResource(name),
                FabricBlockEntityTypeBuilder.create(factory, block).build(null));
    }

    @Override
    public void initialize() {
        FabricDefaultAttributeRegistry.register(ALIEN, ClassicAlienEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(AQUATIC_ALIEN, AquaticAlienEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(AQUATIC_CHESTBURSTER, AquaticChestbursterEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(CHESTBURSTER, ChestbursterEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EGG, AlienEggEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(FACEHUGGER, FacehuggerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(RUNNER_ALIEN, RunnerAlienEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(RUNNERBURSTER, RunnerbursterEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(MUTANT_POPPER, PopperEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(MUTANT_HAMMERPEDE, HammerpedeEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(MUTANT_STALKER, StalkerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(NEOBURSTER, NeobursterEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(NEOMORPH_ADOLESCENT, NeomorphAdolescentEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(NEOMORPH, NeomorphEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(SPITTER, SpitterEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(DRACONICTEMPLEBEAST, DraconicTempleBeastEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(RAVENOUSTEMPLEBEAST, RavenousTempleBeastEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(MOONLIGHTHORRORTEMPLEBEAST,
                MoonlightHorrorTempleBeastEntity.createAttributes());
    }

    public static BlockEntityType<AlienStorageEntity> ALIEN_STORAGE_BLOCK_ENTITY_1 = registerBlockType(
            "alien_storage_block_entity", AlienStorageEntity::new, GigBlocks.ALIEN_STORAGE_BLOCK_1);
    public static BlockEntityType<AlienStorageHuggerEntity> ALIEN_STORAGE_BLOCK_ENTITY_1_HUGGER = registerBlockType(
            "alien_storage_block_entity_hugger", AlienStorageHuggerEntity::new, GigBlocks.ALIEN_STORAGE_BLOCK_1_HUGGER);
    public static BlockEntityType<AlienStorageGooEntity> ALIEN_STORAGE_BLOCK_ENTITY_1_GOO = registerBlockType(
            "alien_storage_block_entity_goo", AlienStorageGooEntity::new, GigBlocks.ALIEN_STORAGE_BLOCK_1_GOO);
    public static BlockEntityType<AlienStorageSporeEntity> ALIEN_STORAGE_BLOCK_ENTITY_1_SPORE = registerBlockType(
            "alien_storage_block_entity_spore", AlienStorageSporeEntity::new, GigBlocks.ALIEN_STORAGE_BLOCK_1_SPORE);
    public static BlockEntityType<JarStorageEntity> ALIEN_STORAGE_BLOCK_ENTITY_2 = registerBlockType(
            "alien_storage_jar_entity", JarStorageEntity::new, GigBlocks.ALIEN_STORAGE_BLOCK_2);
    public static BlockEntityType<IdolStorageEntity> ALIEN_STORAGE_BLOCK_ENTITY_3 = registerBlockType(
            "sitting_idol_entity", IdolStorageEntity::new, GigBlocks.ALIEN_STORAGE_BLOCK_3);
    public static BlockEntityType<SporeBlockEntity> SPORE_ENTITY = registerBlockType("neomorph_spore_pods",
            SporeBlockEntity::new, GigBlocks.SPORE_BLOCK);
    public static BlockEntityType<PetrifiedOjbectEntity> PETRIFIED_OBJECT = registerBlockType("petrified_object",
            PetrifiedOjbectEntity::new, GigBlocks.PETRIFIED_OBJECT_BLOCK);


}
