package mods.cybercat.gigeresque.common.entity;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageEntity;
import mods.cybercat.gigeresque.common.block.entity.IdolStorageEntity;
import mods.cybercat.gigeresque.common.block.entity.JarStorageEntity;
import mods.cybercat.gigeresque.common.entity.impl.AlienEggEntity;
import mods.cybercat.gigeresque.common.entity.impl.AquaticAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.AquaticChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.ChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.ClassicAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.FacehuggerEntity;
import mods.cybercat.gigeresque.common.entity.impl.RunnerAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.RunnerbursterEntity;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

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

	private static <T extends Entity> EntityType<T> registerAlienType(String name, EntityType.EntityFactory<T> factory,
			float width, float height) {
		return Registry.register(Registry.ENTITY_TYPE, new Identifier(Gigeresque.MOD_ID, name), FabricEntityTypeBuilder
				.create(SpawnGroup.MONSTER, factory).dimensions(EntityDimensions.fixed(width, height)).trackedUpdateRate(1).build());
	}

	public static final EntityType<? extends ClassicAlienEntity> ALIEN = registerAlienType(
			EntityIdentifiers.ALIEN.getPath(), ClassicAlienEntity::new, 1.0f, 2.45f);
	public static final EntityType<? extends AquaticAlienEntity> AQUATIC_ALIEN = registerAlienType(
			EntityIdentifiers.AQUATIC_ALIEN.getPath(), AquaticAlienEntity::new, 2.0f, 2.0f);
	public static final EntityType<? extends AquaticChestbursterEntity> AQUATIC_CHESTBURSTER = registerAlienType(
			EntityIdentifiers.AQUATIC_CHESTBURSTER.getPath(), AquaticChestbursterEntity::new, 0.5f, 0.25f);
	public static final EntityType<? extends ChestbursterEntity> CHESTBURSTER = registerAlienType(
			EntityIdentifiers.CHESTBURSTER.getPath(), ChestbursterEntity::new, 0.5f, 0.25f);
	public static final EntityType<? extends AlienEggEntity> EGG = registerAlienType(EntityIdentifiers.EGG.getPath(),
			AlienEggEntity::new, 0.7f, 0.7f);
	public static final EntityType<? extends FacehuggerEntity> FACEHUGGER = registerAlienType(
			EntityIdentifiers.FACEHUGGER.getPath(), FacehuggerEntity::new, 0.5f, 0.3f);
	public static final EntityType<? extends RunnerAlienEntity> RUNNER_ALIEN = registerAlienType(
			EntityIdentifiers.RUNNER_ALIEN.getPath(), RunnerAlienEntity::new, 1.25f, 1.75f);
	public static final EntityType<? extends RunnerbursterEntity> RUNNERBURSTER = registerAlienType(
			EntityIdentifiers.RUNNERBURSTER.getPath(), RunnerbursterEntity::new, 0.5f, 0.5f);

	public static BlockEntityType<AlienStorageEntity> ALIEN_STORAGE_BLOCK_ENTITY_1;
	public static BlockEntityType<JarStorageEntity> ALIEN_STORAGE_BLOCK_ENTITY_2;
	public static BlockEntityType<IdolStorageEntity> ALIEN_STORAGE_BLOCK_ENTITY_3;

	@Override
	public void initialize() {
		ALIEN_STORAGE_BLOCK_ENTITY_1 = Registry.register(Registry.BLOCK_ENTITY_TYPE,
				Gigeresque.MOD_ID + ":alien_storage_block_entity", FabricBlockEntityTypeBuilder
						.create(AlienStorageEntity::new, GIgBlocks.ALIEN_STORAGE_BLOCK_1).build(null));
		ALIEN_STORAGE_BLOCK_ENTITY_2 = Registry.register(Registry.BLOCK_ENTITY_TYPE,
				Gigeresque.MOD_ID + ":alien_storage_jar_entity", FabricBlockEntityTypeBuilder
						.create(JarStorageEntity::new, GIgBlocks.ALIEN_STORAGE_BLOCK_2).build(null));
		ALIEN_STORAGE_BLOCK_ENTITY_3 = Registry.register(Registry.BLOCK_ENTITY_TYPE,
				Gigeresque.MOD_ID + ":sitting_idol_entity", FabricBlockEntityTypeBuilder
						.create(IdolStorageEntity::new, GIgBlocks.ALIEN_STORAGE_BLOCK_3).build(null));
		FabricDefaultAttributeRegistry.register(ALIEN, ClassicAlienEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(AQUATIC_ALIEN, AquaticAlienEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(AQUATIC_CHESTBURSTER, AquaticChestbursterEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(CHESTBURSTER, ChestbursterEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(EGG, AlienEggEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(FACEHUGGER, FacehuggerEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(RUNNER_ALIEN, RunnerAlienEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(RUNNERBURSTER, RunnerbursterEntity.createAttributes());
	}
}
