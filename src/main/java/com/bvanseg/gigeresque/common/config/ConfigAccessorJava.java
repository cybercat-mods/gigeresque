package com.bvanseg.gigeresque.common.config;

import com.bvanseg.gigeresque.common.GigeresqueJava;
import com.bvanseg.gigeresque.common.entity.EntityIdentifiers;

import java.util.*;
import java.util.function.Function;

public class ConfigAccessorJava {
    private static Map<String, HashSet<String>> mappedAcidResistantBlocks;
    private static Map<String, String> reversedMorphMappings;
    private static Map<EntityIdentifiersJava, List<String>> whitelistMappings;
    private static Map<EntityIdentifiersJava, List<String>> blackListMappings;

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

}
