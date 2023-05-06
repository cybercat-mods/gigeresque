package mods.cybercat.gigeresque.common.config;

import java.util.Arrays;
import java.util.List;

import eu.midnightdust.lib.config.MidnightConfig;

public class GigeresqueConfig extends MidnightConfig {

	@Entry
	public static boolean facehuggerGivesBlindness = false;

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

	@Entry
	public static float acidDamage = 2;

	@Entry
	public static int maxSurgeryKitUses = 4;

	@Entry
	public static int surgeryKitCooldownTicks = 15;

	@Entry
	public static int xenoMaxSoundRange = 48;

	@Entry
	public static double classicXenoHealth = 100;

	@Entry
	public static double classicXenoArmor = 6;

	@Entry
	public static double classicXenoAttackDamage = 7;

	@Entry
	public static float classicXenoTailAttackDamage = 3;

	@Entry
	public static float classicXenoAttackSpeed = 3.0F;

	@Entry
	public static double aquaticXenoHealth = 90;

	@Entry
	public static double aquaticXenoArmor = 4;

	@Entry
	public static double aquaticXenoAttackDamage = 7;

	@Entry
	public static float aquaticXenoTailAttackDamage = 3;

	@Entry
	public static double hammerpedeHealth = 15;

	@Entry
	public static double hammerpedeAttackDamage = 1.5;

	@Entry
	public static double popperHealth = 15;

	@Entry
	public static double popperAttackDamage = 3;

	@Entry
	public static double runnerbusterHealth = 15;

	@Entry
	public static double runnerbusterAttackDamage = 5;

	@Entry
	public static double stalkerXenoHealth = 60;

	@Entry
	public static double stalkerXenoArmor = 2;

	@Entry
	public static double stalkerAttackDamage = 5;

	@Entry
	public static float stalkerTailAttackDamage = 3;

	@Entry
	public static float stalkerAttackSpeed = 1.7F;

	@Entry
	public static double runnerXenoHealth = 80;

	@Entry
	public static double runnerXenoArmor = 4;

	@Entry
	public static double runnerXenoAttackDamage = 7;

	@Entry
	public static float runnerXenoTailAttackDamage = 3;

	@Entry
	public static float runnerXenoAttackSpeed = 3.0F;

	@Entry
	public static double alieneggHealth = 20;

	@Entry
	public static double alieneggHatchRange = 7;

	@Entry
	public static double chestbursterHealth = 15;

	@Entry
	public static double facehuggerHealth = 20;

	@Entry
	public static double spitterXenoHealth = 60;

	@Entry
	public static double spitterXenoArmor = 2;

	@Entry
	public static double spitterAttackDamage = 5;

	@Entry
	public static double spitterRangeAttackDamage = 3;

	@Entry
	public static double neobursterXenoHealth = 60;

	@Entry
	public static double neobursterAttackDamage = 5;

	@Entry
	public static double neomorph_adolescentXenoHealth = 30;

	@Entry
	public static double neomorph_adolescentAttackDamage = 7;

	@Entry
	public static double neomorphXenoHealth = 60;

	@Entry
	public static double neomorphXenoArmor = 2;

	@Entry
	public static double neomorphAttackDamage = 9;

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
	public static List<String> alienegg_biomes = Arrays.asList("");

	@Entry
	public static int alienegg_spawn_weight = 10;
	@Entry
	public static int alienegg_min_group = 1;
	@Entry
	public static int alienegg_max_group = 1;
}
