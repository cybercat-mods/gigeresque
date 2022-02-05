package com.bvanseg.gigeresque.common.config

import com.bvanseg.gigeresque.common.Gigeresque
import com.bvanseg.gigeresque.common.entity.EntityIdentifiers
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.util.registry.Registry
import java.util.*
import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 */
object ConfigAccessor {

    val mappedAcidResistantBlocks: Map<String, Set<String>> by lazy { processAcidResistantBlocks() }

    val reversedMorphMappings: Map<String, String> by lazy { processReversedMorphMappings() }

    private val whitelistMappings by lazy {
        mapOf(
            EntityIdentifiers.ALIEN to Gigeresque.config.targeting.alienWhitelist,
            EntityIdentifiers.AQUATIC_ALIEN to Gigeresque.config.targeting.aquaticAlienWhitelist,
            EntityIdentifiers.FACEHUGGER to Gigeresque.config.targeting.facehuggerWhitelist,
            EntityIdentifiers.RUNNER_ALIEN to Gigeresque.config.targeting.runnerWhitelist
        )
    }

    private val blacklistMappings by lazy {
        mapOf(
            EntityIdentifiers.ALIEN to Gigeresque.config.targeting.alienBlacklist,
            EntityIdentifiers.AQUATIC_ALIEN to Gigeresque.config.targeting.aquaticAlienBlacklist,
            EntityIdentifiers.FACEHUGGER to Gigeresque.config.targeting.facehuggerBlacklist,
            EntityIdentifiers.RUNNER_ALIEN to Gigeresque.config.targeting.runnerBlacklist
        )
    }

    fun isTargetWhitelisted(entity: LivingEntity, target: Entity?) = isTargetWhitelisted(entity::class, target)
    fun isTargetBlacklisted(entity: LivingEntity, target: Entity?) = isTargetBlacklisted(entity::class, target)

    fun isTargetWhitelisted(entityClass: KClass<*>, target: Entity?): Boolean {
        if (target == null) return false
        val attackerIdentifier = EntityIdentifiers.typeMappings[entityClass] ?: return false
        val whitelist = whitelistMappings[attackerIdentifier] ?: return false
        val targetIdentifier = Registry.ENTITY_TYPE.getId(target.type)
        return whitelist.contains(targetIdentifier.toString())
    }

    fun isTargetBlacklisted(entityClass: KClass<*>, target: Entity?): Boolean {
        if (target == null) return false
        val attackerIdentifier = EntityIdentifiers.typeMappings[entityClass] ?: return false
        val blacklist = blacklistMappings[attackerIdentifier] ?: return false
        val targetIdentifier = Registry.ENTITY_TYPE.getId(target.type)
        return blacklist.contains(targetIdentifier.toString())
    }

    private fun processAcidResistantBlocks(): Map<String, Set<String>> {
        val map = hashMapOf<String, HashSet<String>>()
        Gigeresque.config.miscellaneous.acidResistantBlocks.forEach {
            val parts = it.lowercase(Locale.US).split(":")

            if (parts.size == 1) {
                map.computeIfAbsent("minecraft") { hashSetOf() }.add(parts[0])
            } else if (parts.size == 2) {
                map.computeIfAbsent(parts[0]) { hashSetOf() }.add(parts[1])
            }
        }

        return map
    }

    private fun processReversedMorphMappings(): Map<String, String> {
        val map = hashMapOf<String, String>()

        val internalMap = mapOf(
            EntityIdentifiers.ALIEN.toString() to Gigeresque.config.morphing.alienHosts,
            EntityIdentifiers.AQUATIC_ALIEN.toString() to Gigeresque.config.morphing.aquaticAlienHosts,
            EntityIdentifiers.RUNNER_ALIEN.toString() to Gigeresque.config.morphing.runnerHosts
        )

        internalMap.forEach { (morphTo, morphFromSet) ->
            morphFromSet.forEach { morphFrom ->
                map.computeIfAbsent(morphFrom) { morphTo }
            }
        }
        return map
    }
}