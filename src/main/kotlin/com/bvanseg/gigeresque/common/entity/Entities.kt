package com.bvanseg.gigeresque.common.entity

import com.bvanseg.gigeresque.CustomSpawnGroup
import com.bvanseg.gigeresque.common.Gigeresque
import com.bvanseg.gigeresque.common.entity.impl.*
import com.bvanseg.gigeresque.common.util.GigeresqueInitializer
import com.bvanseg.gigeresque.common.util.initializingBlock
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

/**
 * @author Boston Vanseghi
 */
object Entities : GigeresqueInitializer {

    private fun <T : Entity> registerAlienType(name: String, factory: EntityType.EntityFactory<T>, width: Float = 1.0f, height: Float = 1.0f): EntityType<T> {
        return Registry.register(
            Registry.ENTITY_TYPE,
            Identifier(Gigeresque.MOD_ID, name),
            FabricEntityTypeBuilder.create(CustomSpawnGroup.ALIEN, factory)
                .dimensions(EntityDimensions.fixed(width, height)).build()
        )
    }

    val ALIEN = registerAlienType(EntityIdentifiers.ALIEN.path, ::ClassicAlienEntity, 1.0f, 2.85f)
    val AQUATIC_ALIEN = registerAlienType(EntityIdentifiers.AQUATIC_ALIEN.path, ::AquaticAlienEntity, 2.0f, 2.0f)
    val AQUATIC_CHESTBURSTER = registerAlienType(EntityIdentifiers.AQUATIC_CHESTBURSTER.path, ::AquaticChestbursterEntity, 0.5f, 0.25f)
    val CHESTBURSTER = registerAlienType(EntityIdentifiers.CHESTBURSTER.path, ::ChestbursterEntity, 0.5f, 0.25f)
    val EGG = registerAlienType(EntityIdentifiers.EGG.path, ::AlienEggEntity, 0.7f, 0.7f)
    val FACEHUGGER = registerAlienType(EntityIdentifiers.FACEHUGGER.path, ::FacehuggerEntity, 0.5f, 0.3f)
    val RUNNER_ALIEN = registerAlienType(EntityIdentifiers.RUNNER_ALIEN.path, ::RunnerAlienEntity, 1.75f, 1.75f)
    val RUNNERBURSTER = registerAlienType(EntityIdentifiers.RUNNERBURSTER.path, ::RunnerbursterEntity, 0.5f, 0.5f)

    override fun initialize() = initializingBlock("AlienTypes") {
        FabricDefaultAttributeRegistry.register(ALIEN, ClassicAlienEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(AQUATIC_ALIEN, AquaticAlienEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(AQUATIC_CHESTBURSTER, AquaticChestbursterEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(CHESTBURSTER, ChestbursterEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(EGG, AlienEggEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(FACEHUGGER, FacehuggerEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(RUNNER_ALIEN, RunnerAlienEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(RUNNERBURSTER, RunnerbursterEntity.createAttributes())
    }
}