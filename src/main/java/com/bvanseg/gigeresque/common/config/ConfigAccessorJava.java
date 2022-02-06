package com.bvanseg.gigeresque.common.config;

import com.bvanseg.gigeresque.common.GigeresqueJava;
import com.bvanseg.gigeresque.common.entity.EntityIdentifiers;
import com.bvanseg.gigeresque.common.entity.EntityIdentifiersJava;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;
import java.util.function.Function;

public class ConfigAccessorJava {
    private static Map<String, HashSet<String>> mappedAcidResistantBlocks;
    private static Map<String, String> reversedMorphMappings;
    private static Map<Identifier, List<String>> whitelistMappings;
    private static Map<Identifier, List<String>> blacklistMappings;

    private synchronized static Map<Identifier, List<String>> getWhitelistMappings() {
        if (whitelistMappings == null) {
            whitelistMappings = Map.of(
                    EntityIdentifiersJava.ALIEN, GigeresqueJava.config.targeting.alienWhitelist,
                    EntityIdentifiersJava.AQUATIC_ALIEN, GigeresqueJava.config.targeting.aquaticAlienWhitelist,
                    EntityIdentifiersJava.FACEHUGGER, GigeresqueJava.config.targeting.facehuggerWhitelist,
                    EntityIdentifiersJava.RUNNER_ALIEN, GigeresqueJava.config.targeting.runnerWhitelist
            );
        }
        return whitelistMappings;
    }

    private synchronized static Map<Identifier, List<String>> getBlacklistMappings() {
        if (blacklistMappings == null) {
            blacklistMappings = Map.of(
                    EntityIdentifiersJava.ALIEN, GigeresqueJava.config.targeting.alienBlacklist,
                    EntityIdentifiersJava.AQUATIC_ALIEN, GigeresqueJava.config.targeting.aquaticAlienBlacklist,
                    EntityIdentifiersJava.FACEHUGGER, GigeresqueJava.config.targeting.facehuggerBlacklist,
                    EntityIdentifiersJava.RUNNER_ALIEN, GigeresqueJava.config.targeting.runnerBlacklist
            );
        }
        return blacklistMappings;
    }

    public synchronized static Map<String, HashSet<String>> getMappedAcidResistantBlocks() {
        if (mappedAcidResistantBlocks == null) {
            processAcidResistantBlocks();
        }
        return mappedAcidResistantBlocks;
    }

    private static void processAcidResistantBlocks() {
        HashMap<String, HashSet<String>> map = new HashMap<>();
        GigeresqueJava.config.miscellaneous.acidResistantBlocks.forEach((it) -> {
            String[] parts = it.toLowerCase(Locale.US).split(":");

            if (parts.length == 1) {
                map.computeIfAbsent("minecraft", s -> new HashSet<>()).add(parts[0]);
            } else if (parts.length == 2) {
                map.computeIfAbsent(parts[0], s -> new HashSet<>()).add(parts[1]);
            }
        });

        mappedAcidResistantBlocks = map;
    }

    public static boolean isTargetWhitelisted(LivingEntity entity, Entity target) {
        return isTargetWhitelisted(entity.getClass(), target);
    }

    public static boolean isTargetWhitelisted(Class<? extends Entity> entityClass, Entity target) {
        if (target == null) return false;
        Identifier attackerIdentifier = EntityIdentifiersJava.typeMappings.get(entityClass);
        List<String> whitelist = getWhitelistMappings().getOrDefault(attackerIdentifier, Collections.emptyList());
        Identifier targetIdentifier = Registry.ENTITY_TYPE.getId(target.getType());
        return whitelist.contains(targetIdentifier.toString());
    }

    public static boolean isTargetBlacklisted(LivingEntity entity, Entity target) {
        return isTargetBlacklisted(entity.getClass(), target);
    }

    public static boolean isTargetBlacklisted(Class<? extends Entity> entityClass, Entity target) {
        if (target == null) return false;
        Identifier attackerIdentifier = EntityIdentifiersJava.typeMappings.get(entityClass);
        List<String> blacklist = getBlacklistMappings().getOrDefault(attackerIdentifier, Collections.emptyList());
        Identifier targetIdentifier = Registry.ENTITY_TYPE.getId(target.getType());
        return blacklist.contains(targetIdentifier.toString());
    }

}
