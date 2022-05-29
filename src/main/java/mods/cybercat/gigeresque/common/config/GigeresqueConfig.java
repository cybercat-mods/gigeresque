package mods.cybercat.gigeresque.common.config;

import java.util.List;

import mods.cybercat.gigeresque.Constants;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;

public class GigeresqueConfig extends CustomMidnightConfig {
	@Entry
	public static boolean isolationMode = false;

	public boolean getIsolationMode() {
		return isolationMode;
	}

	@Entry
	public static float alienGrowthMultiplier = 1.0f;

	@Entry
	public static int alienSpawnCap = Constants.ALIEN_SPAWN_CAP;

	@Entry
	public static float aquaticAlienGrowthMultiplier = 1.0f;

	@Entry
	public static float aquaticChestbursterGrowthMultiplier = 1.0f;

	@Entry
	public static float chestbursterGrowthMultiplier = 1.0f;

	@Entry
	public static float eggmorphTickMultiplier = 1.0f;

	@Entry
	public static float facehuggerAttachTickTimer = 4800.0f;

	@Entry
	public static float impregnationTickTimer = 9600.0f;

	@Entry
	public static float runnerAlienGrowthMultiplier = 1.0f;

	@Entry
	public static float runnerbursterGrowthMultiplier = 1.0f;

	@Entry
	public static int gooEffectTickTimer = 6000;

	public static float getAlienGrowthMultiplier() {
		return alienGrowthMultiplier;
	}

	public static int getAlienSpawnCap() {
		return alienSpawnCap;
	}

	public static float getAquaticAlienGrowthMultiplier() {
		return aquaticAlienGrowthMultiplier;
	}

	public static float getAquaticChestbursterGrowthMultiplier() {
		return aquaticChestbursterGrowthMultiplier;
	}

	public static float getChestbursterGrowthMultiplier() {
		return chestbursterGrowthMultiplier;
	}

	public static float getEggmorphTickMultiplier() {
		return eggmorphTickMultiplier;
	}

	public static float getFacehuggerAttachTickTimer() {
		return facehuggerAttachTickTimer;
	}

	public static float getImpregnationTickTimer() {
		return impregnationTickTimer;
	}

	public static float getRunnerAlienGrowthMultiplier() {
		return runnerAlienGrowthMultiplier;
	}

	public static float getRunnerbursterGrowthMultiplier() {
		return runnerbursterGrowthMultiplier;
	}

	public static int getgooEffectTickTimer() {
		return gooEffectTickTimer;
	}

	@Entry
	public static List<String> alienHosts = List.of(Registry.ENTITY_TYPE.getId(EntityType.EVOKER).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.ILLUSIONER).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.PILLAGER).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.PIGLIN).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.PIGLIN_BRUTE).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.PLAYER).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.WITCH).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.VILLAGER).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.VINDICATOR).toTranslationKey());

	@Entry
	public static List<String> aquaticAlienHosts = List.of(Registry.ENTITY_TYPE.getId(EntityType.DOLPHIN).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.TURTLE).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.GUARDIAN).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.ELDER_GUARDIAN).toTranslationKey());

	@Entry
	public static List<String> runnerHosts = List.of(Registry.ENTITY_TYPE.getId(EntityType.COW).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.DONKEY).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.FOX).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.GOAT).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.HOGLIN).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.HORSE).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.LLAMA).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.MOOSHROOM).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.MULE).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.PANDA).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.PIG).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.OCELOT).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.POLAR_BEAR).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.RAVAGER).toTranslationKey(), // TODO: Remove when ubermorphs added
			Registry.ENTITY_TYPE.getId(EntityType.SHEEP).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.WOLF).toTranslationKey());

	@Entry
	public static List<String> dnaBlacklist = List.of(Registry.ENTITY_TYPE.getId(EntityType.BLAZE).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.ENDER_DRAGON).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.ENDERMAN).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.ENDERMITE).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.GHAST).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.SHULKER).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.IRON_GOLEM).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.MAGMA_CUBE).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.SLIME).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.SILVERFISH).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.SNOW_GOLEM).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.VEX).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.WITHER).toTranslationKey());

	@Entry
	public static List<String> alienWhitelist = List.of(Registry.ENTITY_TYPE.getId(EntityType.PLAYER).toTranslationKey());

	@Entry
	public static List<String> alienBlacklist = List.of(Registry.ENTITY_TYPE.getId(EntityType.BAT).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.BEE).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.CAT).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.SPIDER).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.CAVE_SPIDER).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.CREEPER).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.TROPICAL_FISH).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.PUFFERFISH).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.COD).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.SALMON).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.PARROT).toTranslationKey());

	@Entry
	public static List<String> aquaticAlienWhitelist = List.of("");

	@Entry
	public static List<String> aquaticAlienBlacklist = List.of(Registry.ENTITY_TYPE.getId(EntityType.BAT).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.BEE).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.CAT).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.CREEPER).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.TROPICAL_FISH).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.PUFFERFISH).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.COD).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.SALMON).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.PARROT).toTranslationKey());

	@Entry
	public static List<String> facehuggerWhitelist = List.of(Registry.ENTITY_TYPE.getId(EntityType.PLAYER).toTranslationKey());

	@Entry
	public static List<String> facehuggerBlacklist = List.of(Registry.ENTITY_TYPE.getId(EntityType.BLAZE).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.CAVE_SPIDER).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.CHICKEN).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.COD).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.BEE).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.BAT).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.SHULKER).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.CREEPER).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.ENDER_DRAGON).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.ENDERMAN).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.ENDERMITE).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.GHAST).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.GLOW_SQUID).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.IRON_GOLEM).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.MAGMA_CUBE).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.PARROT).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.PUFFERFISH).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.RABBIT).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.SALMON).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.SLIME).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.SILVERFISH).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.SNOW_GOLEM).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.SPIDER).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.SQUID).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.TROPICAL_FISH).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.VEX).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.WITHER).toTranslationKey());

	@Entry
	public static List<String> runnerWhitelist = List.of("");

	@Entry
	public static List<String> runnerBlacklist = List.of(Registry.ENTITY_TYPE.getId(EntityType.BAT).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.BEE).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.CAT).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.TROPICAL_FISH).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.PUFFERFISH).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.COD).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.SALMON).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.CREEPER).toTranslationKey(),
			Registry.ENTITY_TYPE.getId(EntityType.PARROT).toTranslationKey());
}
