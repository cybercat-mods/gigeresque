package mods.cybercat.gigeresque.common.config;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
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
	public static List<String> alienHosts = List.of(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.EVOKER).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.ILLUSIONER).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PILLAGER).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PIGLIN).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PIGLIN_BRUTE).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PLAYER).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.WITCH).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.VILLAGER).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.VINDICATOR).toString());

	@Entry
	public static List<String> aquaticAlienHosts = List.of(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.DOLPHIN).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.TURTLE).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.GUARDIAN).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.ELDER_GUARDIAN).toString());

	@Entry
	public static List<String> runnerHosts = List.of(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.COW).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.DONKEY).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.FOX).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.GOAT).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.HOGLIN).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.HORSE).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.LLAMA).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.MOOSHROOM).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.MULE).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PANDA).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PIG).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.OCELOT).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.POLAR_BEAR).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.RAVAGER).toString(), // TODO: Remove when ubermorphs added
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.SHEEP).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.WOLF).toString());

	@Entry
	public static List<String> dnaBlacklist = List.of(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.BLAZE).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.ENDER_DRAGON).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.ENDERMAN).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.ENDERMITE).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.GHAST).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.SHULKER).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.IRON_GOLEM).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.MAGMA_CUBE).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.SLIME).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.SILVERFISH).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.SNOW_GOLEM).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.VEX).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.WITHER).toString());

	@Entry
	public static List<String> alienWhitelist = List.of(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PLAYER).toString());

	@Entry
	public static List<String> alienBlacklist = List.of(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.BAT).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.BEE).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.CAT).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.SPIDER).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.CAVE_SPIDER).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.CREEPER).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.TROPICAL_FISH).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PUFFERFISH).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.COD).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.SALMON).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PARROT).toString());

	@Entry
	public static List<String> aquaticAlienWhitelist = List.of("");

	@Entry
	public static List<String> aquaticAlienBlacklist = List.of(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.BAT).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.BEE).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.CAT).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.CREEPER).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.TROPICAL_FISH).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PUFFERFISH).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.COD).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.SALMON).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PARROT).toString());

	@Entry
	public static List<String> facehuggerWhitelist = List.of(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PLAYER).toString());

	@Entry
	public static List<String> facehuggerBlacklist = List.of(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.BLAZE).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.CAVE_SPIDER).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.CHICKEN).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.COD).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.BEE).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.BAT).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.SHULKER).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.CREEPER).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.ENDER_DRAGON).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.ENDERMAN).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.ENDERMITE).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.GHAST).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.GLOW_SQUID).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.IRON_GOLEM).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.MAGMA_CUBE).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PARROT).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PUFFERFISH).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.RABBIT).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.SALMON).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.SLIME).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.SILVERFISH).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.SNOW_GOLEM).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.SPIDER).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.SQUID).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.TROPICAL_FISH).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.VEX).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.WITHER).toString());

	@Entry
	public static List<String> runnerWhitelist = List.of("");

	@Entry
	public static List<String> runnerBlacklist = List.of(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.BAT).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.BEE).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.CAT).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.TROPICAL_FISH).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PUFFERFISH).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.COD).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.SALMON).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.CREEPER).toString(),
			BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PARROT).toString());
}
