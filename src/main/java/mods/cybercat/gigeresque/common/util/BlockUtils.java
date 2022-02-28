package mods.cybercat.gigeresque.common.util;

import java.util.HashSet;

import mods.cybercat.gigeresque.common.config.ConfigAccessor;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockUtils {
	private BlockUtils() {
	}

	public static boolean isBlockAcidResistant(Block block) {
		Identifier id = Registry.BLOCK.getId(block);
		String path = id.getPath();
		String namespace = id.getNamespace();
		return ConfigAccessor.getMappedAcidResistantBlocks().getOrDefault(namespace, new HashSet<>()).contains(path);
	}
}
