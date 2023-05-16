package mods.cybercat.gigeresque.common.entity;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageEntity;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageGooEntity;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageHuggerEntity;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageSporeEntity;
import mods.cybercat.gigeresque.common.block.entity.IdolStorageEntity;
import mods.cybercat.gigeresque.common.block.entity.JarStorageEntity;
import mods.cybercat.gigeresque.common.block.entity.SporeBlockEntity;
import mods.cybercat.gigeresque.common.entity.impl.RunnerAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.RunnerbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticChestbursterEntity;
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
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class Entities implements GigeresqueInitializer {
	private Entities() {
	}

	private static Entities instance;

	synchronized public static Entities getInstance() {
		if (instance == null) {
			instance = new Entities();
		}
		return instance;
	}

	private static <T extends Entity> EntityType<T> registerAlienType(String name, EntityType.EntityFactory<T> factory, float width, float height) {
		return Registry.register(Registry.ENTITY_TYPE, Constants.modResource(name), FabricEntityTypeBuilder.create(MobCategory.MONSTER, factory).dimensions(EntityDimensions.fixed(width, height)).trackedUpdateRate(1).build());
	}

	public static final EntityType<? extends ClassicAlienEntity> ALIEN = registerAlienType(EntityIdentifiers.ALIEN.getPath(), ClassicAlienEntity::new, 0.9f, 2.45f);
	public static final EntityType<? extends AquaticAlienEntity> AQUATIC_ALIEN = registerAlienType(EntityIdentifiers.AQUATIC_ALIEN.getPath(), AquaticAlienEntity::new, 2.0f, 2.0f);
	public static final EntityType<? extends AquaticChestbursterEntity> AQUATIC_CHESTBURSTER = registerAlienType(EntityIdentifiers.AQUATIC_CHESTBURSTER.getPath(), AquaticChestbursterEntity::new, 0.5f, 0.25f);
	public static final EntityType<? extends ChestbursterEntity> CHESTBURSTER = registerAlienType(EntityIdentifiers.CHESTBURSTER.getPath(), ChestbursterEntity::new, 0.5f, 0.25f);
	public static final EntityType<? extends AlienEggEntity> EGG = registerAlienType(EntityIdentifiers.EGG.getPath(), AlienEggEntity::new, 0.7f, 0.9f);
	public static final EntityType<? extends FacehuggerEntity> FACEHUGGER = registerAlienType(EntityIdentifiers.FACEHUGGER.getPath(), FacehuggerEntity::new, 0.5f, 0.3f);
	public static final EntityType<? extends RunnerAlienEntity> RUNNER_ALIEN = registerAlienType(EntityIdentifiers.RUNNER_ALIEN.getPath(), RunnerAlienEntity::new, 1.25f, 1.75f);
	public static final EntityType<? extends RunnerbursterEntity> RUNNERBURSTER = registerAlienType(EntityIdentifiers.RUNNERBURSTER.getPath(), RunnerbursterEntity::new, 0.5f, 0.5f);
	public static final EntityType<? extends PopperEntity> MUTANT_POPPER = registerAlienType(EntityIdentifiers.MUTANT_POPPER.getPath(), PopperEntity::new, 1.0f, 0.75f);
	public static final EntityType<? extends HammerpedeEntity> MUTANT_HAMMERPEDE = registerAlienType(EntityIdentifiers.MUTANT_HAMMERPEDE.getPath(), HammerpedeEntity::new, 1.4f, 0.75f);
	public static final EntityType<? extends StalkerEntity> MUTANT_STALKER = registerAlienType(EntityIdentifiers.MUTANT_STALKER.getPath(), StalkerEntity::new, 1.25f, 1.75f);

	public static final EntityType<? extends NeobursterEntity> NEOBURSTER = registerAlienType(EntityIdentifiers.NEOBURSTER.getPath(), NeobursterEntity::new, 1.0f, 0.75f);
	public static final EntityType<? extends NeomorphAdolescentEntity> NEOMORPH_ADOLESCENT = registerAlienType(EntityIdentifiers.NEOMORPH_ADOLESCENT.getPath(), NeomorphAdolescentEntity::new, 1.15f, 1.65f);
	public static final EntityType<? extends NeomorphEntity> NEOMORPH = registerAlienType(EntityIdentifiers.NEOMORPH.getPath(), NeomorphEntity::new, 0.9f, 3.25f);
	public static final EntityType<? extends SpitterEntity> SPITTER = registerAlienType(EntityIdentifiers.SPITTER.getPath(), SpitterEntity::new, 0.9f, 3.35f);

//	public static final EntityType<? extends CrusherEntity> CRUSHER = registerAlienType(EntityIdentifiers.CRUSHER.getPath(), CrusherEntity::new, 1.3f, 2.35f);
//	public static final EntityType<? extends PraetorianEntity> PRAETORIAN = registerAlienType(EntityIdentifiers.PRAETORIAN.getPath(), PraetorianEntity::new, 1.3f, 3.45f);
//	public static final EntityType<? extends UltramorpthEntity> ULTRAMORTH = registerAlienType(EntityIdentifiers.ULTRAMORTH.getPath(), UltramorpthEntity::new, 1.7f, 4.45f);

	public static BlockEntityType<AlienStorageEntity> ALIEN_STORAGE_BLOCK_ENTITY_1;
	public static BlockEntityType<AlienStorageHuggerEntity> ALIEN_STORAGE_BLOCK_ENTITY_1_HUGGER;
	public static BlockEntityType<AlienStorageGooEntity> ALIEN_STORAGE_BLOCK_ENTITY_1_GOO;
	public static BlockEntityType<AlienStorageSporeEntity> ALIEN_STORAGE_BLOCK_ENTITY_1_SPORE;
	public static BlockEntityType<JarStorageEntity> ALIEN_STORAGE_BLOCK_ENTITY_2;
	public static BlockEntityType<IdolStorageEntity> ALIEN_STORAGE_BLOCK_ENTITY_3;
	public static BlockEntityType<SporeBlockEntity> SPORE_ENTITY;

	@Override
	public void initialize() {
		ALIEN_STORAGE_BLOCK_ENTITY_1 = Registry.register(Registry.BLOCK_ENTITY_TYPE, Constants.modResource("alien_storage_block_entity"), FabricBlockEntityTypeBuilder.create(AlienStorageEntity::new, GIgBlocks.ALIEN_STORAGE_BLOCK_1).build(null));
		ALIEN_STORAGE_BLOCK_ENTITY_1_GOO = Registry.register(Registry.BLOCK_ENTITY_TYPE, Constants.modResource("alien_storage_block_entity_goo"), FabricBlockEntityTypeBuilder.create(AlienStorageGooEntity::new, GIgBlocks.ALIEN_STORAGE_BLOCK_1_GOO).build(null));
		ALIEN_STORAGE_BLOCK_ENTITY_1_HUGGER = Registry.register(Registry.BLOCK_ENTITY_TYPE, Constants.modResource("alien_storage_block_entity_hugger"), FabricBlockEntityTypeBuilder.create(AlienStorageHuggerEntity::new, GIgBlocks.ALIEN_STORAGE_BLOCK_1_HUGGER).build(null));
		ALIEN_STORAGE_BLOCK_ENTITY_1_SPORE = Registry.register(Registry.BLOCK_ENTITY_TYPE, Constants.modResource("alien_storage_block_entity_spore"), FabricBlockEntityTypeBuilder.create(AlienStorageSporeEntity::new, GIgBlocks.ALIEN_STORAGE_BLOCK_1_SPORE).build(null));
		ALIEN_STORAGE_BLOCK_ENTITY_2 = Registry.register(Registry.BLOCK_ENTITY_TYPE, Constants.modResource("alien_storage_jar_entity"), FabricBlockEntityTypeBuilder.create(JarStorageEntity::new, GIgBlocks.ALIEN_STORAGE_BLOCK_2).build(null));
		ALIEN_STORAGE_BLOCK_ENTITY_3 = Registry.register(Registry.BLOCK_ENTITY_TYPE, Constants.modResource("sitting_idol_entity"), FabricBlockEntityTypeBuilder.create(IdolStorageEntity::new, GIgBlocks.ALIEN_STORAGE_BLOCK_3).build(null));
		SPORE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, Constants.modResource("neomorph_spore_pods"), FabricBlockEntityTypeBuilder.create(SporeBlockEntity::new, GIgBlocks.SPORE_BLOCK).build(null));
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

//		FabricDefaultAttributeRegistry.register(CRUSHER, CrusherEntity.createAttributes());
//		FabricDefaultAttributeRegistry.register(PRAETORIAN, PraetorianEntity.createAttributes());
//		FabricDefaultAttributeRegistry.register(ULTRAMORTH, UltramorpthEntity.createAttributes());
//		FabricDefaultAttributeRegistry.register(SPITTER, SpitterEntity.createAttributes());
	}
}
