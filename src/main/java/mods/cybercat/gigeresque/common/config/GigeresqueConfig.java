package mods.cybercat.gigeresque.common.config;

import mod.azure.azurelib.config.Config;
import mod.azure.azurelib.config.Configurable;
import mods.cybercat.gigeresque.common.Gigeresque;

@Config(id = Gigeresque.MOD_ID)
public class GigeresqueConfig {

	@Configurable
	@Configurable.Synchronized
	public boolean facehuggerGivesBlindness = false;

	@Configurable
	@Configurable.Synchronized
	@Configurable.Range(min = 1)
	public float alienblockHardness = 3.0f;

	@Configurable
	@Configurable.Synchronized
	@Configurable.Range(min = 1)
	public float alienblockResistance = 6.0f;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public float alienGrowthMultiplier = 1.0f;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public float aquaticAlienGrowthMultiplier = 1.0f;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public float aquaticChestbursterGrowthMultiplier = 1.0f;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public float chestbursterGrowthMultiplier = 1.0f;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public float eggmorphTickTimer = 6000.0f;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public float facehuggerAttachTickTimer = 4800.0f;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public float impregnationTickTimer = 9600.0f;

	@Configurable
	@Configurable.Synchronized
	@Configurable.Range(min = 1)
	public int sporeTickTimer = 4800;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public float runnerAlienGrowthMultiplier = 1.0f;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public float runnerbursterGrowthMultiplier = 1.0f;

	@Configurable
	@Configurable.Synchronized
	@Configurable.Range(min = 1)
	public int gooEffectTickTimer = 6000;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public float acidDamage = 2;

	@Configurable
	@Configurable.Synchronized
	@Configurable.Range(min = 1)
	public int maxSurgeryKitUses = 4;

	@Configurable
	@Configurable.Synchronized
	@Configurable.Range(min = 1)
	public int surgeryKitCooldownTicks = 15;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public int xenoMaxSoundRange = 48;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double classicXenoHealth = 100;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double classicXenoArmor = 6;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double classicXenoAttackDamage = 7;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public float classicXenoTailAttackDamage = 3;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public float classicXenoAttackSpeed = 3.5F;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double aquaticXenoHealth = 90;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double aquaticXenoArmor = 4;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double aquaticXenoAttackDamage = 7;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public float aquaticXenoTailAttackDamage = 3;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double hammerpedeHealth = 15;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double hammerpedeAttackDamage = 1.5;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double popperHealth = 15;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double popperAttackDamage = 3;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double runnerbusterHealth = 15;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double runnerbusterAttackDamage = 5;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double stalkerXenoHealth = 60;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double stalkerXenoArmor = 2;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double stalkerAttackDamage = 5;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public float stalkerTailAttackDamage = 3;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public float stalkerAttackSpeed = 1.7F;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double runnerXenoHealth = 80;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double runnerXenoArmor = 4;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double runnerXenoAttackDamage = 7;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public float runnerXenoTailAttackDamage = 3;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public float runnerXenoAttackSpeed = 3.0F;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double alieneggHealth = 20;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double alieneggHatchRange = 7;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double chestbursterHealth = 15;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double facehuggerHealth = 20;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double spitterXenoHealth = 60;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double spitterXenoArmor = 2;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double spitterAttackDamage = 5;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double spitterRangeAttackDamage = 3;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double neobursterXenoHealth = 60;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double neobursterAttackDamage = 5;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double neomorph_adolescentXenoHealth = 30;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double neomorph_adolescentAttackDamage = 7;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double neomorphXenoHealth = 60;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double neomorphXenoArmor = 2;

	@Configurable
	@Configurable.Synchronized
	@Configurable.DecimalRange(min = 1)
	public double neomorphAttackDamage = 9;

	@Configurable
	@Configurable.Synchronized
	@Configurable.Range(min = 1)
	public int alienegg_spawn_weight = 10;
	
	@Configurable
	@Configurable.Synchronized
	@Configurable.Range(min = 1)
	public int alienegg_min_group = 1;
	
	@Configurable
	@Configurable.Synchronized
	@Configurable.Range(min = 1)
	public int alienegg_max_group = 1;

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
}
