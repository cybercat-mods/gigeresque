package mods.cybercat.gigeresque.common.config;

import mod.azure.azurelib.common.api.common.config.Config;
import mod.azure.azurelib.common.internal.common.config.Configurable;
import mods.cybercat.gigeresque.CommonMod;

@Config(id = CommonMod.MOD_ID)
public class GigeresqueConfig {

    @Configurable
    @Configurable.Synchronized
    public boolean enableDevparticles = false;

    @Configurable
    @Configurable.Synchronized
    public boolean enableDevEntites = false;

    @Configurable
    @Configurable.Synchronized
    public boolean enabledCreativeBootAcidProtection = false;

    @Configurable
    @Configurable.Synchronized
    public boolean enableAcidLavaRemoval = false;

    @Configurable
    @Configurable.Synchronized
    public boolean blackfuildNonrepacle = false;

    @Configurable
    @Configurable.Synchronized
    public FacehuggerConfigs facehuggerConfigs = new FacehuggerConfigs();

    public static class FacehuggerConfigs {
        @Configurable
        @Configurable.Synchronized
        public boolean facehuggerGivesBlindness = false;

        @Configurable
        @Configurable.Synchronized
        public boolean enableFacehuggerAttachmentTimer = true;

        @Configurable
        @Configurable.Synchronized
        public boolean enableFacehuggerTimerTicks = false;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1200)
        public float facehuggerAttachTickTimer = 1200.0f;

        @Configurable
        @Configurable.Synchronized
        @Configurable.Range(min = 1)
        public int facehuggerStunTickTimer = 90;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double facehuggerHealth = 40;
    }

    @Configurable
    @Configurable.Synchronized
    public BlockConfigs alienblockConfigs = new BlockConfigs();

    public static class BlockConfigs {

        @Configurable
        @Configurable.Synchronized
        @Configurable.Range(min = 1)
        public float alienblockHardness = 3.0f;

        @Configurable
        @Configurable.Synchronized
        @Configurable.Range(min = 1)
        public float alienblockResistance = 6.0f;
    }

    @Configurable
    @Configurable.Synchronized
    public BursterConfigs bursterConfigs = new BursterConfigs();

    public static class BursterConfigs {
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
        public float runnerbursterGrowthMultiplier = 1.0f;
    }

    @Configurable
    @Configurable.Synchronized
    @Configurable.DecimalRange(min = 1)
    public float eggmorphTickTimer = 1200.0f;

    @Configurable
    @Configurable.Synchronized
    @Configurable.DecimalRange(min = 1200)
    public float impregnationTickTimer = 1200.0f;

    @Configurable
    @Configurable.Synchronized
    @Configurable.Range(min = 1200)
    public int sporeTickTimer = 1200;

    @Configurable
    @Configurable.Synchronized
    @Configurable.Range(min = 1200)
    public int gooEffectTickTimer = 1200;

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
    public classicConfigs classicXenoConfigs = new classicConfigs();

    public static class classicConfigs {

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public float alienGrowthMultiplier = 1.0f;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double classicXenoHealth = 200;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double classicXenoArmor = 9;

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
        public float classicXenoAttackSpeed = 3.9F;
    }

    @Configurable
    @Configurable.Synchronized
    public AquaticAlienConfigs aquaticXenoConfigs = new AquaticAlienConfigs();

    public static class AquaticAlienConfigs {

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public float aquaticAlienGrowthMultiplier = 1.0f;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double aquaticXenoHealth = 190;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double aquaticXenoArmor = 6;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double aquaticXenoAttackDamage = 7;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public float aquaticXenoTailAttackDamage = 3;
    }

    @Configurable
    @Configurable.Synchronized
    public HammerpedeConfigs hammerpedeConfigs = new HammerpedeConfigs();

    public static class HammerpedeConfigs {

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double hammerpedeHealth = 30;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double hammerpedeAttackDamage = 1.5;
    }

    @Configurable
    @Configurable.Synchronized
    public PopperConfigs popperConfigs = new PopperConfigs();

    public static class PopperConfigs {

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double popperHealth = 30;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double popperAttackDamage = 3;
    }

    @Configurable
    @Configurable.Synchronized
    public RBusterConfigs runnerbusterConfigs = new RBusterConfigs();

    public static class RBusterConfigs {
        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public float runnerAlienGrowthMultiplier = 1.0f;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double runnerbusterHealth = 30;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double runnerbusterAttackDamage = 5;
    }

    @Configurable
    @Configurable.Synchronized
    public StalkerConfigs stalkerConfigs = new StalkerConfigs();

    public static class StalkerConfigs {
        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double stalkerXenoHealth = 120;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double stalkerXenoArmor = 4;

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
    }

    @Configurable
    @Configurable.Synchronized
    public RunnerConfigs runnerConfigs = new RunnerConfigs();

    public static class RunnerConfigs {
        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double runnerXenoHealth = 160;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double runnerXenoArmor = 6;

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
    }

    @Configurable
    @Configurable.Synchronized
    public EggConfigs eggConfigs = new EggConfigs();

    public static class EggConfigs {
        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double alieneggHealth = 40;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double alieneggHatchRange = 7;

        @Configurable
        @Configurable.Synchronized
        @Configurable.Range(min = 0)
        public int alienegg_spawn_weight = 10;

        @Configurable
        @Configurable.Synchronized
        @Configurable.Range(min = 0)
        public int alienegg_min_group = 1;

        @Configurable
        @Configurable.Synchronized
        @Configurable.Range(min = 1)
        public int alienegg_max_group = 1;
    }

    @Configurable
    @Configurable.Synchronized
    @Configurable.DecimalRange(min = 1)
    public double chestbursterHealth = 30;

    @Configurable
    @Configurable.Synchronized
    @Configurable.DecimalRange(min = 1)
    public double facehuggerHealth = 40;

    @Configurable
    @Configurable.Synchronized
    public SpitterConfigs spitterConfigs = new SpitterConfigs();

    public static class SpitterConfigs {
        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double spitterXenoHealth = 120;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double spitterXenoArmor = 4;

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
        public float spitterXenoTailAttackDamage = 3;
    }

    @Configurable
    @Configurable.Synchronized
    public NeobursterConfigs neobursterConfigs = new NeobursterConfigs();

    public static class NeobursterConfigs {

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double neobursterXenoHealth = 120;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double neobursterAttackDamage = 5;
    }

    @Configurable
    @Configurable.Synchronized
    public NeoAdolescentConfigs neomorphAdolescentConfigs = new NeoAdolescentConfigs();

    public static class NeoAdolescentConfigs {

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double neomorph_adolescentXenoHealth = 60;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double neomorph_adolescentAttackDamage = 7;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public float neomorph_adolescentXenoTailAttackDamage = 3;
    }

    @Configurable
    @Configurable.Synchronized
    public NeomorphConfigs neomorphConfigs = new NeomorphConfigs();

    public static class NeomorphConfigs {

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double neomorphXenoHealth = 120;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double neomorphXenoArmor = 4;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double neomorphAttackDamage = 9;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public float neomorphXenoTailAttackDamage = 3;
    }

    @Configurable
    @Configurable.Synchronized
    public DTBConfigs draconicTempleBeastConfigs = new DTBConfigs();

    public static class DTBConfigs {

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double draconicTempleBeastXenoHealth = 300;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double draconicTempleBeastXenoArmor = 9;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double draconicTempleBeastAttackDamage = 9;
    }

    @Configurable
    @Configurable.Synchronized
    public MHTBConfigs moonlightHorrorTempleBeastConfigs = new MHTBConfigs();

    public static class MHTBConfigs {

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double moonlightHorrorTempleBeastXenoHealth = 300;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double moonlightHorrorTempleBeastXenoArmor = 9;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double moonlightHorrorTempleBeastAttackDamage = 9;
    }

    @Configurable
    @Configurable.Synchronized
    public RTBConfigs ravenousTempleBeastConfigs = new RTBConfigs();

    public static class RTBConfigs {

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double ravenousTempleBeastXenoHealth = 300;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double ravenousTempleBeastXenoArmor = 9;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double ravenousTempleBeastAttackDamage = 9;
    }

    @Configurable
    @Configurable.Synchronized
    public BaphomorphConfigs baphomorphConfigs = new BaphomorphConfigs();

    public static class BaphomorphConfigs {

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double baphomorphXenoHealth = 300;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double baphomorphXenoArmor = 9;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double baphomorphAttackDamage = 9;
    }

    @Configurable
    @Configurable.Synchronized
    public HellmorphRunnerConfigs hellmorphrunnerConfigs = new HellmorphRunnerConfigs();

    public static class HellmorphRunnerConfigs {

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double hellmorph_runnerXenoHealth = 300;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double hellmorph_runnerXenoArmor = 9;

        @Configurable
        @Configurable.Synchronized
        @Configurable.DecimalRange(min = 1)
        public double hellmorph_runnerAttackDamage = 9;
    }

    public float getAlienGrowthMultiplier() {
        return classicXenoConfigs.alienGrowthMultiplier;
    }

    public float getAquaticAlienGrowthMultiplier() {
        return aquaticXenoConfigs.aquaticAlienGrowthMultiplier;
    }

    public float getAquaticChestbursterGrowthMultiplier() {
        return bursterConfigs.aquaticChestbursterGrowthMultiplier;
    }

    public float getChestbursterGrowthMultiplier() {
        return bursterConfigs.chestbursterGrowthMultiplier;
    }

    public float getEggmorphTickTimer() {
        return eggmorphTickTimer;
    }

    public float getFacehuggerAttachTickTimer() {
        return facehuggerConfigs.facehuggerAttachTickTimer;
    }

    public float getImpregnationTickTimer() {
        return impregnationTickTimer;
    }

    public int getSporeTickTimer() {
        return sporeTickTimer;
    }

    public float getRunnerAlienGrowthMultiplier() {
        return runnerbusterConfigs.runnerAlienGrowthMultiplier;
    }

    public float getRunnerbursterGrowthMultiplier() {
        return bursterConfigs.runnerbursterGrowthMultiplier;
    }

    public int getgooEffectTickTimer() {
        return gooEffectTickTimer;
    }
}
