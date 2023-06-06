package mods.cybercat.gigeresque.common.config;

import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import mods.cybercat.gigeresque.common.Gigeresque;

@Config(id = Gigeresque.MOD_ID)
public class GigeresqueConfig {

	@Configurable
	public boolean facehuggerGivesBlindness = false;

	@Configurable
	public float alienGrowthMultiplier = 1.0f;

	@Configurable
	public float aquaticAlienGrowthMultiplier = 1.0f;

	@Configurable
	public float aquaticChestbursterGrowthMultiplier = 1.0f;

	@Configurable
	public float chestbursterGrowthMultiplier = 1.0f;

	@Configurable
	public float eggmorphTickTimer = 6000.0f;

	@Configurable
	public float facehuggerAttachTickTimer = 4800.0f;

	@Configurable
	public float impregnationTickTimer = 9600.0f;

	@Configurable
	public int sporeTickTimer = 4800;

	@Configurable
	public float runnerAlienGrowthMultiplier = 1.0f;

	@Configurable
	public float runnerbursterGrowthMultiplier = 1.0f;

	@Configurable
	public int gooEffectTickTimer = 6000;

	@Configurable
	public float acidDamage = 2;

	@Configurable
	public int maxSurgeryKitUses = 4;

	@Configurable
	public int surgeryKitCooldownTicks = 15;

	@Configurable
	public int xenoMaxSoundRange = 48;

	@Configurable
	public double classicXenoHealth = 100;

	@Configurable
	public double classicXenoArmor = 6;

	@Configurable
	public double classicXenoAttackDamage = 7;

	@Configurable
	public float classicXenoTailAttackDamage = 3;

	@Configurable
	public float classicXenoAttackSpeed = 3.5F;

	@Configurable
	public double aquaticXenoHealth = 90;

	@Configurable
	public double aquaticXenoArmor = 4;

	@Configurable
	public double aquaticXenoAttackDamage = 7;

	@Configurable
	public float aquaticXenoTailAttackDamage = 3;

	@Configurable
	public double hammerpedeHealth = 15;

	@Configurable
	public double hammerpedeAttackDamage = 1.5;

	@Configurable
	public double popperHealth = 15;

	@Configurable
	public double popperAttackDamage = 3;

	@Configurable
	public double runnerbusterHealth = 15;

	@Configurable
	public double runnerbusterAttackDamage = 5;

	@Configurable
	public double stalkerXenoHealth = 60;

	@Configurable
	public double stalkerXenoArmor = 2;

	@Configurable
	public double stalkerAttackDamage = 5;

	@Configurable
	public float stalkerTailAttackDamage = 3;

	@Configurable
	public float stalkerAttackSpeed = 1.7F;

	@Configurable
	public double runnerXenoHealth = 80;

	@Configurable
	public double runnerXenoArmor = 4;

	@Configurable
	public double runnerXenoAttackDamage = 7;

	@Configurable
	public float runnerXenoTailAttackDamage = 3;

	@Configurable
	public float runnerXenoAttackSpeed = 3.0F;

	@Configurable
	public double alieneggHealth = 20;

	@Configurable
	public double alieneggHatchRange = 7;

	@Configurable
	public double chestbursterHealth = 15;

	@Configurable
	public double facehuggerHealth = 20;

	@Configurable
	public double spitterXenoHealth = 60;

	@Configurable
	public double spitterXenoArmor = 2;

	@Configurable
	public double spitterAttackDamage = 5;

	@Configurable
	public double spitterRangeAttackDamage = 3;

	@Configurable
	public double neobursterXenoHealth = 60;

	@Configurable
	public double neobursterAttackDamage = 5;

	@Configurable
	public double neomorph_adolescentXenoHealth = 30;

	@Configurable
	public double neomorph_adolescentAttackDamage = 7;

	@Configurable
	public double neomorphXenoHealth = 60;

	@Configurable
	public double neomorphXenoArmor = 2;

	@Configurable
	public double neomorphAttackDamage = 9;

	public float getAlienGrowthMultiplier() {
		return alienGrowthMultiplier;
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

	public float getEggmorphTickTimer() {
		return eggmorphTickTimer;
	}

	public float getFacehuggerAttachTickTimer() {
		return facehuggerAttachTickTimer;
	}

	public float getImpregnationTickTimer() {
		return impregnationTickTimer;
	}

	public int getSporeTickTimer() {
		return sporeTickTimer;
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

	@Configurable
	public String[] alienegg_biomes = { "" };

	@Configurable
	public int alienegg_spawn_weight = 10;
	@Configurable
	public int alienegg_min_group = 1;
	@Configurable
	public int alienegg_max_group = 1;
}
