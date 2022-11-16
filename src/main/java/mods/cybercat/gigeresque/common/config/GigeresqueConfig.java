package mods.cybercat.gigeresque.common.config;

import java.util.List;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;

public class GigeresqueConfig extends CustomMidnightConfig {

	@Entry
	public static float alienGrowthMultiplier = 1.0f;

	@Entry
	public static float aquaticAlienGrowthMultiplier = 1.0f;

	@Entry
	public static float aquaticChestbursterGrowthMultiplier = 1.0f;

	@Entry
	public static float chestbursterGrowthMultiplier = 1.0f;

	@Entry
	public static float eggmorphTickTimer = 6000.0f;

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

	public static float getEggmorphTickTimer() {
		return eggmorphTickTimer;
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
	public static List<String> alienHosts = List.of(Registry.ENTITY_TYPE.getKey(EntityType.EVOKER).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.ILLUSIONER).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.PILLAGER).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.PIGLIN).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.PIGLIN_BRUTE).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.PLAYER).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.WITCH).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.VILLAGER).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.VINDICATOR).toString());

	@Entry
	public static List<String> aquaticAlienHosts = List.of(Registry.ENTITY_TYPE.getKey(EntityType.DOLPHIN).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.TURTLE).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.GUARDIAN).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.ELDER_GUARDIAN).toString());

	@Entry
	public static List<String> runnerHosts = List.of(Registry.ENTITY_TYPE.getKey(EntityType.COW).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.DONKEY).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.FOX).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.GOAT).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.HOGLIN).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.HORSE).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.LLAMA).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.MOOSHROOM).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.MULE).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.PANDA).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.PIG).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.OCELOT).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.POLAR_BEAR).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.RAVAGER).toString(), // TODO: Remove when ubermorphs added
			Registry.ENTITY_TYPE.getKey(EntityType.SHEEP).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.WOLF).toString());

	@Entry
	public static List<String> dnaBlacklist = List.of(Registry.ENTITY_TYPE.getKey(EntityType.BLAZE).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.ENDER_DRAGON).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.ENDERMAN).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.ENDERMITE).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.GHAST).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.SHULKER).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.IRON_GOLEM).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.MAGMA_CUBE).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.SLIME).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.SILVERFISH).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.SNOW_GOLEM).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.VEX).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.WITHER).toString());

	@Entry
	public static List<String> alienWhitelist = List.of(Registry.ENTITY_TYPE.getKey(EntityType.PLAYER).toString());

	@Entry
	public static List<String> alienBlacklist = List.of(Registry.ENTITY_TYPE.getKey(EntityType.BAT).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.BEE).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.CAT).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.SPIDER).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.CAVE_SPIDER).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.CREEPER).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.TROPICAL_FISH).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.PUFFERFISH).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.COD).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.SALMON).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.PARROT).toString());

	@Entry
	public static List<String> aquaticAlienWhitelist = List.of("");

	@Entry
	public static List<String> aquaticAlienBlacklist = List.of(Registry.ENTITY_TYPE.getKey(EntityType.BAT).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.BEE).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.CAT).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.CREEPER).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.TROPICAL_FISH).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.PUFFERFISH).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.COD).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.SALMON).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.PARROT).toString());

	@Entry
	public static List<String> facehuggerWhitelist = List.of(Registry.ENTITY_TYPE.getKey(EntityType.PLAYER).toString());

	@Entry
	public static List<String> facehuggerBlacklist = List.of(Registry.ENTITY_TYPE.getKey(EntityType.BLAZE).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.CAVE_SPIDER).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.CHICKEN).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.COD).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.BEE).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.BAT).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.SHULKER).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.CREEPER).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.ENDER_DRAGON).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.ENDERMAN).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.ENDERMITE).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.GHAST).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.GLOW_SQUID).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.IRON_GOLEM).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.MAGMA_CUBE).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.PARROT).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.PUFFERFISH).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.RABBIT).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.SALMON).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.SLIME).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.SILVERFISH).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.SNOW_GOLEM).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.SPIDER).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.SQUID).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.TROPICAL_FISH).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.VEX).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.WITHER).toString());

	@Entry
	public static List<String> runnerWhitelist = List.of("");

	@Entry
	public static List<String> runnerBlacklist = List.of(Registry.ENTITY_TYPE.getKey(EntityType.BAT).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.BEE).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.CAT).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.TROPICAL_FISH).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.PUFFERFISH).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.COD).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.SALMON).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.CREEPER).toString(),
			Registry.ENTITY_TYPE.getKey(EntityType.PARROT).toString());
}
