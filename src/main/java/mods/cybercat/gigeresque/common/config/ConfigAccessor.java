package mods.cybercat.gigeresque.common.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mods.cybercat.gigeresque.common.entity.EntityIdentifiers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ConfigAccessor {
	private static Map<String, String> reversedMorphMappings;
	private static Map<ResourceLocation, List<String>> whitelistMappings;
	private static Map<ResourceLocation, List<String>> blacklistMappings;

	private synchronized static Map<ResourceLocation, List<String>> getWhitelistMappings() {
		if (whitelistMappings == null) {
			whitelistMappings = Map.of(EntityIdentifiers.ALIEN, GigeresqueConfig.alienWhitelist,
					EntityIdentifiers.AQUATIC_ALIEN, GigeresqueConfig.aquaticAlienWhitelist,
					EntityIdentifiers.FACEHUGGER, GigeresqueConfig.facehuggerWhitelist, EntityIdentifiers.RUNNER_ALIEN,
					GigeresqueConfig.runnerWhitelist);
		}
		return whitelistMappings;
	}

	private synchronized static Map<ResourceLocation, List<String>> getBlacklistMappings() {
		if (blacklistMappings == null) {
			blacklistMappings = Map.of(EntityIdentifiers.ALIEN, GigeresqueConfig.alienBlacklist,
					EntityIdentifiers.AQUATIC_ALIEN, GigeresqueConfig.aquaticAlienBlacklist,
					EntityIdentifiers.FACEHUGGER, GigeresqueConfig.facehuggerBlacklist, EntityIdentifiers.RUNNER_ALIEN,
					GigeresqueConfig.runnerBlacklist);
		}
		return blacklistMappings;
	}

	public static boolean isTargetWhitelisted(LivingEntity entity, Entity target) {
		return isTargetWhitelisted(entity.getClass(), target);
	}

	public static boolean isTargetWhitelisted(Class<? extends Entity> entityClass, Entity target) {
		if (target == null)
			return false;
		ResourceLocation attackerIdentifier = EntityIdentifiers.typeMappings.get(entityClass);
		List<String> whitelist = getWhitelistMappings().getOrDefault(attackerIdentifier, Collections.emptyList());
		ResourceLocation targetIdentifier = BuiltInRegistries.ENTITY_TYPE.getKey(target.getType());
		return whitelist.contains(targetIdentifier.toString());
	}

	public static boolean isTargetBlacklisted(LivingEntity entity, Entity target) {
		return isTargetBlacklisted(entity.getClass(), target);
	}

	public static boolean isTargetHostable(Entity target) {
		List<? extends String> normalEntries = GigeresqueConfig.alienHosts;
		List<? extends String> waterEntries = GigeresqueConfig.aquaticAlienHosts;
		List<? extends String> runnerEntries = GigeresqueConfig.runnerHosts;
		ResourceLocation targetIdentifier = BuiltInRegistries.ENTITY_TYPE.getKey(target.getType());
		return normalEntries.contains(targetIdentifier.toString()) || waterEntries.contains(targetIdentifier.toString())
				|| runnerEntries.contains(targetIdentifier.toString());
	}

	public static boolean isTargetAlienHost(Entity target) {
		List<? extends String> waveEntries = GigeresqueConfig.alienHosts;
		ResourceLocation targetIdentifier = BuiltInRegistries.ENTITY_TYPE.getKey(target.getType());
		return waveEntries.contains(targetIdentifier.toString());
	}

	public static boolean isTargetDNAImmune(Entity target) {
		List<? extends String> waveEntries = GigeresqueConfig.dnaBlacklist;
		ResourceLocation targetIdentifier = BuiltInRegistries.ENTITY_TYPE.getKey(target.getType());
		return waveEntries.contains(targetIdentifier.toString());
	}

	public static boolean isTargetBlacklisted(Class<? extends Entity> entityClass, Entity target) {
		if (target == null)
			return false;
		ResourceLocation attackerIdentifier = EntityIdentifiers.typeMappings.get(entityClass);
		List<String> blacklist = getBlacklistMappings().getOrDefault(attackerIdentifier, Collections.emptyList());
		ResourceLocation targetIdentifier = BuiltInRegistries.ENTITY_TYPE.getKey(target.getType());
		return blacklist.contains(targetIdentifier.toString());
	}

	public static synchronized Map<String, String> getReversedMorphMappings() {
		if (reversedMorphMappings == null) {
			processReversedMorphMappings();
		}
		return reversedMorphMappings;
	}

	private static void processReversedMorphMappings() {
		HashMap<String, String> map = new HashMap<>();

		Map<String, List<String>> internalMap = Map.of(EntityIdentifiers.ALIEN.toString(), GigeresqueConfig.alienHosts,
				EntityIdentifiers.AQUATIC_ALIEN.toString(), GigeresqueConfig.aquaticAlienHosts,
				EntityIdentifiers.RUNNER_ALIEN.toString(), GigeresqueConfig.runnerHosts);

		internalMap.forEach((morphTo, morphFromSet) -> morphFromSet
				.forEach(morphFrom -> map.computeIfAbsent(morphFrom, (it) -> morphTo)));
		reversedMorphMappings = map;
	}

}
