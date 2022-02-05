package com.bvanseg.gigeresque.common.util;

import com.bvanseg.gigeresque.common.config.ConfigAccessorJava;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashSet;

public class BlockUtils {
    private BlockUtils() {
    }

    public static boolean isBlockAcidResistant(Block block) {
        Identifier id = Registry.BLOCK.getId(block);
        String path = id.getPath();
        String namespace = id.getNamespace();
        return ConfigAccessorJava.getMappedAcidResistantBlocks().getOrDefault(namespace, new HashSet<>()).contains(path);
    }
}
