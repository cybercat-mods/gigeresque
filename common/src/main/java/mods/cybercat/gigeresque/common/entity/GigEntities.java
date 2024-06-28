package mods.cybercat.gigeresque.common.entity;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.block.entity.*;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.entity.impl.extra.AcidEntity;
import mods.cybercat.gigeresque.common.entity.impl.extra.GooEntity;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public record GigEntities() implements CommonEntityRegistryInterface, CommonBlockEntityRegistryInterface {

    public static final Supplier<EntityType<ClassicAlienEntity>> ALIEN = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.ALIEN.getPath(), ClassicAlienEntity::new, MobCategory.MONSTER, 0.9f, 2.45f);
    public static final Supplier<EntityType<AquaticAlienEntity>> AQUATIC_ALIEN = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.AQUATIC_ALIEN.getPath(), AquaticAlienEntity::new, MobCategory.MONSTER, 2.0f, 2.0f);
    public static final Supplier<EntityType<AquaticChestbursterEntity>> AQUATIC_CHESTBURSTER = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.AQUATIC_CHESTBURSTER.getPath(), AquaticChestbursterEntity::new, MobCategory.MONSTER, 0.5f, 0.25f);
    public static final Supplier<EntityType<ChestbursterEntity>> CHESTBURSTER = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.CHESTBURSTER.getPath(), ChestbursterEntity::new, MobCategory.MONSTER, 0.5f, 0.25f);
    public static final Supplier<EntityType<AlienEggEntity>> EGG = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.EGG.getPath(), AlienEggEntity::new, MobCategory.MONSTER, 0.7f, 0.9f);
    public static final Supplier<EntityType<FacehuggerEntity>> FACEHUGGER = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.FACEHUGGER.getPath(), FacehuggerEntity::new, MobCategory.MONSTER, 0.95f, 0.3f);
    public static final Supplier<EntityType<RunnerAlienEntity>> RUNNER_ALIEN = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.RUNNER_ALIEN.getPath(), RunnerAlienEntity::new, MobCategory.MONSTER, 1.25f, 1.75f);
    public static final Supplier<EntityType<RunnerbursterEntity>> RUNNERBURSTER = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.RUNNERBURSTER.getPath(), RunnerbursterEntity::new, MobCategory.MONSTER, 0.5f, 0.5f);
    public static final Supplier<EntityType<PopperEntity>> MUTANT_POPPER = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.MUTANT_POPPER.getPath(), PopperEntity::new, MobCategory.MONSTER, 1.0f, 0.75f);
    public static final Supplier<EntityType<HammerpedeEntity>> MUTANT_HAMMERPEDE = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.MUTANT_HAMMERPEDE.getPath(), HammerpedeEntity::new, MobCategory.MONSTER, 1.4f, 0.75f);
    public static final Supplier<EntityType<StalkerEntity>> MUTANT_STALKER = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.MUTANT_STALKER.getPath(), StalkerEntity::new, MobCategory.MONSTER, 1.25f, 1.75f);
    public static final Supplier<EntityType<NeobursterEntity>> NEOBURSTER = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.NEOBURSTER.getPath(), NeobursterEntity::new, MobCategory.MONSTER, 0.5f, 0.45f);
    public static final Supplier<EntityType<NeomorphAdolescentEntity>> NEOMORPH_ADOLESCENT = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.NEOMORPH_ADOLESCENT.getPath(), NeomorphAdolescentEntity::new, MobCategory.MONSTER, 1.0f, 0.9f);
    public static final Supplier<EntityType<NeomorphEntity>> NEOMORPH = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.NEOMORPH.getPath(), NeomorphEntity::new, MobCategory.MONSTER, 0.9f, 2.55f);
    public static final Supplier<EntityType<SpitterEntity>> SPITTER = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.SPITTER.getPath(), SpitterEntity::new, MobCategory.MONSTER, 0.9f, 2.0f);
    public static final Supplier<EntityType<DraconicTempleBeastEntity>> DRACONICTEMPLEBEAST = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.DRACONICTEMPLEBEAST.getPath(), DraconicTempleBeastEntity::new, MobCategory.MONSTER, 1.9f, 2.35f);
    public static final Supplier<EntityType<RavenousTempleBeastEntity>> RAVENOUSTEMPLEBEAST = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.RAVENOUSTEMPLEBEAST.getPath(), RavenousTempleBeastEntity::new, MobCategory.MONSTER, 1.6f, 3.95f);
    public static final Supplier<EntityType<MoonlightHorrorTempleBeastEntity>> MOONLIGHTHORRORTEMPLEBEAST = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.MOONLIGHTHORRORTEMPLEBEAST.getPath(), MoonlightHorrorTempleBeastEntity::new, MobCategory.MONSTER, 2.1f, 4.95f);
    public static final Supplier<EntityType<Entity>> ACID = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.ACID.getPath(), AcidEntity::new, MobCategory.MISC, 0.8f, 0.05f);
    public static final Supplier<EntityType<Entity>> GOO = CommonEntityRegistryInterface.registerEntity(
            CommonMod.MOD_ID, EntityIdentifiers.GOO.getPath(), GooEntity::new, MobCategory.MISC, 0.8f, 0.05f);

    public static final Supplier<BlockEntityType<AlienStorageEntity>> ALIEN_STORAGE_BLOCK_ENTITY_1 = CommonBlockEntityRegistryInterface.registerBlockEntity(
            CommonMod.MOD_ID, "alien_storage_block_entity",
            () -> BlockEntityType.Builder.of(
                    AlienStorageEntity::new,
                    GigBlocks.ALIEN_STORAGE_BLOCK_1.get()).build(null));
    public static final Supplier<BlockEntityType<AlienStorageHuggerEntity>> ALIEN_STORAGE_BLOCK_ENTITY_1_HUGGER = CommonBlockEntityRegistryInterface.registerBlockEntity(
            CommonMod.MOD_ID, "alien_storage_block_entity_hugger",
            () -> BlockEntityType.Builder.of(
                    AlienStorageHuggerEntity::new,
                    GigBlocks.ALIEN_STORAGE_BLOCK_1_HUGGER.get()).build(null));
    public static final Supplier<BlockEntityType<AlienStorageGooEntity>> ALIEN_STORAGE_BLOCK_ENTITY_1_GOO = CommonBlockEntityRegistryInterface.registerBlockEntity(
            CommonMod.MOD_ID, "alien_storage_block_entity_goo",
            () -> BlockEntityType.Builder.of(
                    AlienStorageGooEntity::new,
                    GigBlocks.ALIEN_STORAGE_BLOCK_1_GOO.get()).build(null));
    public static final Supplier<BlockEntityType<AlienStorageSporeEntity>> ALIEN_STORAGE_BLOCK_ENTITY_1_SPORE = CommonBlockEntityRegistryInterface.registerBlockEntity(
            CommonMod.MOD_ID, "alien_storage_block_entity_spore",
            () -> BlockEntityType.Builder.of(
                    AlienStorageSporeEntity::new,
                    GigBlocks.ALIEN_STORAGE_BLOCK_1_SPORE.get()).build(null));
    public static final Supplier<BlockEntityType<JarStorageEntity>> ALIEN_STORAGE_BLOCK_ENTITY_2 = CommonBlockEntityRegistryInterface.registerBlockEntity(
            CommonMod.MOD_ID, "alien_storage_jar_entity",
            () -> BlockEntityType.Builder.of(
                    JarStorageEntity::new,
                    GigBlocks.ALIEN_STORAGE_BLOCK_2.get()).build(null));
    public static final Supplier<BlockEntityType<IdolStorageEntity>> ALIEN_STORAGE_BLOCK_ENTITY_3 = CommonBlockEntityRegistryInterface.registerBlockEntity(
            CommonMod.MOD_ID, "sitting_idol_entity",
            () -> BlockEntityType.Builder.of(
                    IdolStorageEntity::new,
                    GigBlocks.ALIEN_STORAGE_BLOCK_3.get()).build(null));
    public static final Supplier<BlockEntityType<SporeBlockEntity>> SPORE_ENTITY = CommonBlockEntityRegistryInterface.registerBlockEntity(
            CommonMod.MOD_ID, "neomorph_spore_pods",
            () -> BlockEntityType.Builder.of(
                    SporeBlockEntity::new,
                    GigBlocks.SPORE_BLOCK.get()).build(null));
    public static final Supplier<BlockEntityType<PetrifiedOjbectEntity>> PETRIFIED_OBJECT = CommonBlockEntityRegistryInterface.registerBlockEntity(
            CommonMod.MOD_ID, "petrified_object",
            () -> BlockEntityType.Builder.of(
                    PetrifiedOjbectEntity::new,
                    GigBlocks.PETRIFIED_OBJECT_BLOCK.get()).build(null));

    public static void initialize() {
    }
}
