package mods.cybercat.gigeresque.common.config;

import java.util.List;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.Gigeresque;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;

@Config(name = Gigeresque.MOD_ID)
public class GigeresqueConfig implements ConfigData {
	public static class Features {
		@ConfigEntry.Gui.Tooltip(count = 3)
		public boolean isolationMode = false;

		public boolean getIsolationMode() {
			return isolationMode;
		}
	}

	public static class Miscellaneous {
		
		@ConfigEntry.Gui.Tooltip(count = 1)
		public float alienGrowthMultiplier = 1.0f;

		@ConfigEntry.Gui.Tooltip(count = 2)
		public int alienSpawnCap = Constants.ALIEN_SPAWN_CAP;

		@ConfigEntry.Gui.Tooltip(count = 1)
		public float aquaticAlienGrowthMultiplier = 1.0f;

		@ConfigEntry.Gui.Tooltip(count = 1)
		public float aquaticChestbursterGrowthMultiplier = 1.0f;

		@ConfigEntry.Gui.Tooltip(count = 1)
		public float chestbursterGrowthMultiplier = 1.0f;

		@ConfigEntry.Gui.Tooltip(count = 2)
		public float eggmorphTickMultiplier = 1.0f;

		@ConfigEntry.Gui.Tooltip(count = 2)
		public float facehuggerAttachTickTimer = 4800.0f;

		@ConfigEntry.Gui.Tooltip(count = 2)
		public float impregnationTickTimer = 9600.0f;

		@ConfigEntry.Gui.Tooltip(count = 1)
		public float runnerAlienGrowthMultiplier = 1.0f;

		@ConfigEntry.Gui.Tooltip(count = 1)
		public float runnerbursterGrowthMultiplier = 1.0f;
		
		@ConfigEntry.Gui.Tooltip(count = 1)
		public int gooEffectTickTimer = 6000;

		public float getAlienGrowthMultiplier() {
			return alienGrowthMultiplier;
		}

		public int getAlienSpawnCap() {
			return alienSpawnCap;
		}

		public float getAquaticAlienGrowthMultiplier() {
			return aquaticAlienGrowthMultiplier;
		}

		public float getAquaticChestbursterGrowthMultiplier() {
			return aquaticChestbursterGrowthMultiplier;
		}

		public float getChestbursterGrowthMultiplier() {
			return chestbursterGrowthMultiplier;
		}

		public float getEggmorphTickMultiplier() {
			return eggmorphTickMultiplier;
		}

		public float getFacehuggerAttachTickTimer() {
			return facehuggerAttachTickTimer;
		}

		public float getImpregnationTickTimer() {
			return impregnationTickTimer;
		}

		public float getRunnerAlienGrowthMultiplier() {
			return runnerAlienGrowthMultiplier;
		}

		public float getRunnerbursterGrowthMultiplier() {
			return runnerbursterGrowthMultiplier;
		}

		public int getgooEffectTickTimer() {
			return gooEffectTickTimer;
		}
	}

	public static class Morphing {
		@ConfigEntry.Gui.Tooltip(count = 1)
		public List<String> alienHosts = List.of(Registry.ENTITY_TYPE.getId(EntityType.EVOKER).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.ILLUSIONER).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.PILLAGER).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.PIGLIN).toString(), // TODO: Remove when hellmorphs added
				Registry.ENTITY_TYPE.getId(EntityType.PIGLIN_BRUTE).toString(), // TODO: Remove when hellmorphs added
				Registry.ENTITY_TYPE.getId(EntityType.PLAYER).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.WITCH).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.VILLAGER).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.VINDICATOR).toString());

		@ConfigEntry.Gui.Tooltip(count = 1)
		public List<String> aquaticAlienHosts = List.of(Registry.ENTITY_TYPE.getId(EntityType.DOLPHIN).toString());

		@ConfigEntry.Gui.Tooltip(count = 1)
		public List<String> runnerHosts = List.of(Registry.ENTITY_TYPE.getId(EntityType.COW).toString(),
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
				Registry.ENTITY_TYPE.getId(EntityType.POLAR_BEAR).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.RAVAGER).toString(), // TODO: Remove when ubermorphs added
				Registry.ENTITY_TYPE.getId(EntityType.SHEEP).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.WOLF).toString());
		
		@ConfigEntry.Gui.Tooltip(count = 1)
		public List<String> dnaBlacklist = List.of(Registry.ENTITY_TYPE.getId(EntityType.BLAZE).toString(),
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
	}

	public static class Targeting {

		@ConfigEntry.Gui.Tooltip(count = 1)
		public List<String> alienWhitelist = List.of(Registry.ENTITY_TYPE.getId(EntityType.PLAYER).toString());

		@ConfigEntry.Gui.Tooltip(count = 1)
		public List<String> alienBlacklist = List.of(Registry.ENTITY_TYPE.getId(EntityType.BAT).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.BEE).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.CAT).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.CREEPER).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.PARROT).toString());

		@ConfigEntry.Gui.Tooltip(count = 1)
		public List<String> aquaticAlienWhitelist = List.of("");

		@ConfigEntry.Gui.Tooltip(count = 1)
		public List<String> aquaticAlienBlacklist = List.of(Registry.ENTITY_TYPE.getId(EntityType.BAT).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.BEE).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.CAT).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.CREEPER).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.PARROT).toString());

		@ConfigEntry.Gui.Tooltip(count = 1)
		public List<String> facehuggerWhitelist = List.of(Registry.ENTITY_TYPE.getId(EntityType.PLAYER).toString());

		@ConfigEntry.Gui.Tooltip(count = 1)
		public List<String> facehuggerBlacklist = List.of(Registry.ENTITY_TYPE.getId(EntityType.BLAZE).toString(),
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

		@ConfigEntry.Gui.Tooltip(count = 1)
		public List<String> runnerWhitelist = List.of("");

		@ConfigEntry.Gui.Tooltip(count = 1)
		public List<String> runnerBlacklist = List.of(Registry.ENTITY_TYPE.getId(EntityType.BAT).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.BEE).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.CAT).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.CREEPER).toString(),
				Registry.ENTITY_TYPE.getId(EntityType.PARROT).toString());
	}

	@ConfigEntry.Gui.CollapsibleObject
	public Features features = new Features();

	@ConfigEntry.Gui.CollapsibleObject
	public Miscellaneous miscellaneous = new Miscellaneous();

	@ConfigEntry.Gui.CollapsibleObject
	public Morphing morphing = new Morphing();

	@ConfigEntry.Gui.CollapsibleObject
	public Targeting targeting = new Targeting();

	public Features getFeatures() {
		return features;
	}

	public Miscellaneous getMiscellaneous() {
		return miscellaneous;
	}

	public Morphing getMorphing() {
		return morphing;
	}

	public Targeting getTargeting() {
		return targeting;
	}
}
