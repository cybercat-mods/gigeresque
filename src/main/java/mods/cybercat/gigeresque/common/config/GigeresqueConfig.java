package mods.cybercat.gigeresque.common.config;

import java.util.List;

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
	public static List<String> alienHosts = List.of(Registry.ENTITY_TYPE.getId(EntityType.EVOKER).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.ILLUSIONER).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.PILLAGER).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.PIGLIN).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.PIGLIN_BRUTE).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.PLAYER).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.WITCH).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.VILLAGER).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.VINDICATOR).toString());

	@Entry
	public static List<String> aquaticAlienHosts = List.of(Registry.ENTITY_TYPE.getId(EntityType.DOLPHIN).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.TURTLE).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.GUARDIAN).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.ELDER_GUARDIAN).toString());

	@Entry
	public static List<String> runnerHosts = List.of(Registry.ENTITY_TYPE.getId(EntityType.COW).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.DONKEY).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.FOX).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.GOAT).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.HOGLIN).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.HORSE).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.LLAMA).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.MOOSHROOM).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.MULE).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.PANDA).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.PIG).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.OCELOT).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.POLAR_BEAR).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.RAVAGER).toString(), // TODO: Remove when ubermorphs added
			Registry.ENTITY_TYPE.getId(EntityType.SHEEP).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.WOLF).toString());

	@Entry
	public static List<String> dnaBlacklist = List.of(Registry.ENTITY_TYPE.getId(EntityType.BLAZE).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.ENDER_DRAGON).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.ENDERMAN).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.ENDERMITE).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.GHAST).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.SHULKER).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.IRON_GOLEM).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.MAGMA_CUBE).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.SLIME).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.SILVERFISH).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.SNOW_GOLEM).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.VEX).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.WITHER).toString());

	@Entry
	public static List<String> alienWhitelist = List.of(Registry.ENTITY_TYPE.getId(EntityType.PLAYER).toString());

	@Entry
	public static List<String> alienBlacklist = List.of(Registry.ENTITY_TYPE.getId(EntityType.BAT).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.BEE).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.CAT).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.SPIDER).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.CAVE_SPIDER).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.CREEPER).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.TROPICAL_FISH).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.PUFFERFISH).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.COD).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.SALMON).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.PARROT).toString());

	@Entry
	public static List<String> aquaticAlienWhitelist = List.of("");

	@Entry
	public static List<String> aquaticAlienBlacklist = List.of(Registry.ENTITY_TYPE.getId(EntityType.BAT).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.BEE).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.CAT).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.CREEPER).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.TROPICAL_FISH).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.PUFFERFISH).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.COD).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.SALMON).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.PARROT).toString());

	@Entry
	public static List<String> facehuggerWhitelist = List.of(Registry.ENTITY_TYPE.getId(EntityType.PLAYER).toString());

	@Entry
	public static List<String> facehuggerBlacklist = List.of(Registry.ENTITY_TYPE.getId(EntityType.BLAZE).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.CAVE_SPIDER).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.CHICKEN).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.COD).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.BEE).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.BAT).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.SHULKER).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.CREEPER).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.ENDER_DRAGON).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.ENDERMAN).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.ENDERMITE).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.GHAST).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.GLOW_SQUID).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.IRON_GOLEM).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.MAGMA_CUBE).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.PARROT).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.PUFFERFISH).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.RABBIT).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.SALMON).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.SLIME).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.SILVERFISH).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.SNOW_GOLEM).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.SPIDER).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.SQUID).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.TROPICAL_FISH).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.VEX).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.WITHER).toString());

	@Entry
	public static List<String> runnerWhitelist = List.of("");

	@Entry
	public static List<String> runnerBlacklist = List.of(Registry.ENTITY_TYPE.getId(EntityType.BAT).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.BEE).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.CAT).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.TROPICAL_FISH).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.PUFFERFISH).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.COD).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.SALMON).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.CREEPER).toString(),
			Registry.ENTITY_TYPE.getId(EntityType.PARROT).toString());
}
